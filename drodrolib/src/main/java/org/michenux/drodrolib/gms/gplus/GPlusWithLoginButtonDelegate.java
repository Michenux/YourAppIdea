package org.michenux.drodrolib.gms.gplus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import org.michenux.drodrolib.gms.GmsUtils;
import org.michenux.drodrolib.security.UserHelper;

public class GPlusWithLoginButtonDelegate extends GPlusDelegate {

    private SignInButton mPlusSignInButton;

    private View mSignOutView;
    private Button mSignOutButton;
    private Button mDisconnectButton;

    private View mProgressView;

    public GPlusWithLoginButtonDelegate(Activity activity, UserHelper userHelper ) {
        super(activity, userHelper);
    }

    public void initViews( SignInButton signInButton, View signOutView, Button signOutButton, Button disconnectButton, View progressViewId) {
        this.mPlusSignInButton = signInButton;
        this.mSignOutButton = signOutButton;
        this.mDisconnectButton = disconnectButton;
        this.mSignOutView = signOutView;
        this.mProgressView = progressViewId;

        if (GmsUtils.supportsGooglePlayServices(this.getActivity())) {
            // Set a listener to connect the user when the G+ button is clicked.
            mPlusSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        } else {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    protected void onPlusClientRevokeAccess() {
        // Access to the user's G+ account has been revoked.  Per the developer terms, delete
        // any stored user data here.
    }

    @Override
    protected void onPlusClientSignIn() {
        //Set up sign out and disconnect buttons.
        if ( mSignOutButton != null ) {
            mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
        }
        if ( mDisconnectButton != null ) {
            mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
        }
    }

    @Override
    protected void onPlusClientSignOut() {
    }

    @Override
    protected void onPlusClientBlockingUI(boolean show) {
        showProgress(show);
    }

    @Override
    protected void updateConnectButtonState() {
          boolean connected = getGoogleApiClient().isConnected();
        if ( mSignOutView != null ) {
            mSignOutView.setVisibility(connected ? View.VISIBLE : View.GONE);
        }
        mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = this.getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

//
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
