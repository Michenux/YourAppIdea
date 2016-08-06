package org.michenux.drodrolib.gms.auth;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.michenux.drodrolib.BuildConfig;
import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;

public class GoogleAuthDelegate implements GoogleApiClient.OnConnectionFailedListener {
    public static final String PROVIDER_NAME = "gplus";

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private UserHelper mUserHelper;
    private UserSessionCallback mUserSessionCallback;

    private GoogleSignInOptions mGso;

    public GoogleAuthDelegate(FragmentActivity activity, UserHelper userHelper) {
        this.mUserHelper = userHelper;

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();
    }

    public void onStart() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            if (BuildConfig.DEBUG) {
                Log.d(MCXApplication.LOG_TAG, "Got cached sign-in");
            }
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, "onConnectionFailed:" + connectionResult);
        }
    }

    public void signIn(FragmentActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, "Google Services: signout");
        }
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mUserHelper.setCurrentUser(null);
                        mUserSessionCallback.onLogout();
                    }
                });
    }

    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mUserHelper.setCurrentUser(null);
                        mUserSessionCallback.onLogout();
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, "handleSignInResult:" + result.isSuccess() + " " + result.getStatus().getStatusCode() +
                    result.getStatus().hasResolution());
        }

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User();
            user.setProvider(PROVIDER_NAME);
            user.setProviderDisplayName("Google+");
            user.setUserId(acct.getId());
            user.setUserName(acct.getId());
            user.setDisplayName(acct.getDisplayName());
            user.setMail(acct.getEmail());

            this.mUserHelper.setCurrentUser(user);

            if (this.mUserSessionCallback != null) {
                this.mUserSessionCallback.onLogin();
            }
        } else {
            // Signed out, show unauthenticated UI.
            this.mUserSessionCallback.onLogout();
        }
    }

    public void setUserSessionCallback(UserSessionCallback userSessionCallback) {
        this.mUserSessionCallback = userSessionCallback;
    }
}
