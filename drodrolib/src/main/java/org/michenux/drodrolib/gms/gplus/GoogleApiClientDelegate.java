package org.michenux.drodrolib.gms.gplus;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;

public class GoogleApiClientDelegate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String PROVIDER_NAME = "gplus";

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    private static final String DIALOG_ERROR = "dialog_error";

    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;

    private UserHelper mUserHelper;
    private UserSessionCallback mUserSessionCallback;

    private boolean mResolvingError = false;

    public GoogleApiClientDelegate(Activity activity, UserHelper userHelper, Bundle savedInstanceState) {

        this.mUserHelper = userHelper;

        mGoogleApiClient =
                new GoogleApiClient.Builder(activity)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_LOGIN)
//                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
    }

    public void onStart() {
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR ) {
            mResolvingError = false;
            if (resultCode == Activity.RESULT_OK && !mGoogleApiClient.isConnected()
                    && !mGoogleApiClient.isConnecting()) {
                // This time, connect should succeed.
                mGoogleApiClient.connect();
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    public void onConnected(Bundle bundle) {

        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        User user = new User();
        user.setProvider(PROVIDER_NAME);
        user.setProviderDisplayName("Google+");
        user.setUserId(currentPerson.getId());
        user.setUserName(currentPerson.getId());
        user.setFirstName(currentPerson.getName().getGivenName());
        user.setLastName(currentPerson.getName().getFamilyName());
        user.setDisplayName(currentPerson.getDisplayName());
        user.setMail(Plus.AccountApi.getAccountName(mGoogleApiClient));

        this.mUserHelper.setCurrentUser(user);

        if (this.mUserSessionCallback != null) {
            this.mUserSessionCallback.onLogin();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mConnectionResult = connectionResult;
    }

    public void signIn( FragmentActivity activity ) {
        if ( mConnectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                mConnectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
        else {
            showErrorDialog(mConnectionResult.getErrorCode(), activity);
            mResolvingError = true;
        }
    }

    public void signOut() {
        Log.d(MCXApplication.LOG_TAG, "gplus: signOut");
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mUserHelper.setCurrentUser(null);
            mUserSessionCallback.onLogout();
            mGoogleApiClient.connect();
        }
    }

    public void revokeAccess() {
        Log.d(MCXApplication.LOG_TAG, "gplus: revoke access");
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi
                    .revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.d(MCXApplication.LOG_TAG,"gplus: revoke access succeeded");
                                mGoogleApiClient.disconnect();
                                mUserHelper.setCurrentUser(null);
                                mUserSessionCallback.onLogout();
                                mGoogleApiClient.connect();
                            }
                            else {
                                Log.e(MCXApplication.LOG_TAG,"gplus: failure revoking access");
                            }
                        }
                    });
        }
    }

    public void setUserSessionCallback(UserSessionCallback userSessionCallback) {
        this.mUserSessionCallback = userSessionCallback;
    }

    public boolean isConnectionResult() {
        return mConnectionResult != null;
    }

    private void showErrorDialog(int errorCode, FragmentActivity activity) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(this);
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(activity.getSupportFragmentManager(), "errordialog");
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {

        private GoogleApiClientDelegate mGoogleApiDelegate;

        public ErrorDialogFragment() {
        }

        public static ErrorDialogFragment newInstance(GoogleApiClientDelegate apiClientDelegate) {
            ErrorDialogFragment fragment = new ErrorDialogFragment();
            fragment.setGoogleApiDelegate(apiClientDelegate);
            return fragment;
        }

        public void setGoogleApiDelegate(GoogleApiClientDelegate delegate) {
            mGoogleApiDelegate = delegate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mGoogleApiDelegate != null) {
                mGoogleApiDelegate.onDialogDismissed();
            }
        }
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }




}
