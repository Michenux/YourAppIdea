package org.michenux.drodrolib.gms.gplus;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;

public class GPlusDelegate implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static final String PROVIDER_NAME = "gplus";

    // A magic number we will use to know that our sign-in error resolution activity has completed
    private static final int OUR_REQUEST_CODE = 49404;

    private Activity mActivity ;

    // This is the helper object that connects to Google Play Services.
    private GoogleApiClient mGoogleApiClient;

    // A flag to stop multiple dialogues appearing for the user
    private boolean mAutoResolveOnFail;

    // A flag to track when a connection is already in progress
    public boolean mPlusClientIsConnecting = false;

    // The saved result from {@link #onConnectionFailed(ConnectionResult)}.  If a connection
    // attempt has been made, this is non-null.
    // If this IS null, then the connect method is still running.
    private ConnectionResult mConnectionResult;

    private UserHelper mUserHelper;

    private UserSessionCallback mUserSessionCallback;


    //-- Activity lifecycle ---------------------------------------------------------------------

    public GPlusDelegate(Activity activity, UserHelper userHelper) {
        this.mActivity = activity;
        this.mUserHelper = userHelper;

        mGoogleApiClient =
                new GoogleApiClient.Builder(mActivity)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_LOGIN)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
    }


    public void onStart() {
        initiatePlusClientConnect();
    }

    public void onStop() {
        initiatePlusClientDisconnect();
    }

    /**
     * An earlier connection failed, and we're now receiving the result of the resolution attempt
     * by PlusClient.
     *
     * @see #onConnectionFailed(ConnectionResult)
     */
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        updateConnectButtonState();
        if (requestCode == OUR_REQUEST_CODE && responseCode == Activity.RESULT_OK) {
            // If we have a successful result, we will want to be able to resolve any further
            // errors, so turn on resolution with our flag.
            mAutoResolveOnFail = true;
            // If we have a successful result, let's call connect() again. If there are any more
            // errors to resolve we'll get our onConnectionFailed, but if not,
            // we'll get onConnected.
            initiatePlusClientConnect();
        } else if (requestCode == OUR_REQUEST_CODE && responseCode != Activity.RESULT_OK) {
            // If we've got an error we can't resolve, we're no longer in the midst of signing
            // in, so we can stop the progress spinner.
            setProgressBarVisible(false);
        }
    }

    //-- End Activity lifecycle ---------------------------------------------------------------------


    /**
     * Called when the {@link PlusClient} revokes access to this app.
     */
    protected void onPlusClientRevokeAccess() {

    }

    /**
     * Called when the PlusClient is successfully connected.
     */
    protected void onPlusClientSignIn() {

    }

    /**
     * Called when the {@link PlusClient} is disconnected.
     */
    protected void onPlusClientSignOut() {

    }

    /**
     * Called when the {@link PlusClient} is blocking the UI.  If you have a progress bar widget,
     * this tells you when to show or hide it.
     */
    protected void onPlusClientBlockingUI(boolean show) {

    }

    /**
     * Called when there is a change in connection state.  If you have "Sign in"/ "Connect",
     * "Sign out"/ "Disconnect", or "Revoke access" buttons, this lets you know when their states
     * need to be updated.
     */
    protected void updateConnectButtonState() {

    }

    //-------------------------------------------------------------------------------------------

    /**
     * Try to sign in the user.
     */
    public void signIn() {
        if (!mGoogleApiClient.isConnected()) {
            // Show the dialog as we are now signing in.
            setProgressBarVisible(true);
            // Make sure that we will start the resolution (e.g. fire the intent and pop up a
            // dialog for the user) for any errors that come in.
            mAutoResolveOnFail = true;
            // We should always have a connection result ready to resolve,
            // so we can start that process.
            if (mConnectionResult != null) {
                startResolution();
            } else {
                // If we don't have one though, we can start connect in
                // order to retrieve one.
                initiatePlusClientConnect();
            }
        }
        else {
            Log.e(MCXApplication.LOG_TAG,"gplus signIn: already connected");
        }

        updateConnectButtonState();
    }

    public void revokeAccess() {
        Log.d(MCXApplication.LOG_TAG,"gplus: revoke access");
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi
                    .revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.d(MCXApplication.LOG_TAG,"gplus: revoke access succeeded");
                                updateConnectButtonState();
                                onPlusClientRevokeAccess();
                                mUserHelper.setCurrentUser(null);
                                mUserSessionCallback.onLogout();
                            }
                            else {
                                Log.e(MCXApplication.LOG_TAG,"gplus: failure revoking access");
                            }
                        }
                    });
        }
    }

    /**
     * Connect the {@link PlusClient} only if a connection isn't already in progress.  This will
     * call back to {@link #onConnected(android.os.Bundle)} or
     * {@link #onConnectionFailed(com.google.android.gms.common.ConnectionResult)}.
     */
    private void initiatePlusClientConnect() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Disconnect the {@link PlusClient} only if it is connected (otherwise, it can throw an error.)
     * This will call back to {@link #onConnectionSuspended(int)} ()}.
     */
    private void initiatePlusClientDisconnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        updateConnectButtonState();
        setProgressBarVisible(false);

        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        User user = new User();
        user.setProvider(PROVIDER_NAME);
        user.setProviderDisplayName("Google+");
        user.setUserId(currentPerson.getId());
        user.setUserName(currentPerson.getId());
        user.setFirstName(currentPerson.getName().getGivenName());
        user.setLastName(currentPerson.getName().getFamilyName());
        user.setDisplayName(currentPerson.getDisplayName());

        this.mUserHelper.setCurrentUser(user);

        if (this.mUserSessionCallback != null) {
            this.mUserSessionCallback.onLogin();
        }

        onPlusClientSignIn();
    }

    @Override
    public void onConnectionSuspended(int i) {
        updateConnectButtonState();
        this.mUserHelper.setCurrentUser(null);

        if ( this.mUserSessionCallback != null ) {
            this.mUserSessionCallback.onLogout();
        }

        onPlusClientSignOut();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        updateConnectButtonState();

        // Most of the time, the connection will fail with a user resolvable result. We can store
        // that in our mConnectionResult property ready to be used when the user clicks the
        // sign-in button.
        if (connectionResult.hasResolution()) {
            mConnectionResult = connectionResult;
            if (mAutoResolveOnFail) {
                // This is a local helper function that starts the resolution of the problem,
                // which may be showing the user an account chooser or similar.
                startResolution();
            }
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    /**
     * A helper method to flip the mResolveOnFail flag and start the resolution
     * of the ConnectionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a result from the activity we're
            // about to start.
            mAutoResolveOnFail = false;
            // If we can resolve the error, then call start resolution and pass it an integer tag
            // we can use to track.
            // This means that when we get the onActivityResult callback we'll know it's from
            // being started here.
            mConnectionResult.startResolutionForResult(this.mActivity, OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new ConnectionResult.
            mConnectionResult = null;
            initiatePlusClientConnect();
        }
    }

    public boolean isPlusClientConnecting() {
        return mPlusClientIsConnecting;
    }

    private void setProgressBarVisible(boolean flag) {
        mPlusClientIsConnecting = flag;
        onPlusClientBlockingUI(flag);
    }



    public void setUserSessionCallback(UserSessionCallback userSessionCallback) {
        this.mUserSessionCallback = userSessionCallback;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public ConnectionResult getConnectionResult() {
        return mConnectionResult;
    }
}
