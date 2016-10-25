package org.michenux.yourappidea.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.yourappidea.YourApplication;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class FacebookDelegate implements FacebookCallback {
    public static final String PROVIDER_NAME = "facebook";

    @Inject
    UserHelper mUserHelper;

    private Activity mActivity;

    private List<String> mPermissions;

    private CallbackManager mCallbackManager;

    private AccessTokenTracker mAccessTokenTracker;
    private AccessToken mAccessToken;

    private ProfileTracker mProfileTracker;

    private UserSessionCallback mUserSessionCallback;

    public FacebookDelegate(UserHelper userHelper, Activity activity, String... permissions) {
        this.mUserHelper = userHelper;

        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mActivity = activity;
        mPermissions = Arrays.asList(permissions);
        LoginManager.getInstance().registerCallback(mCallbackManager, this);

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                mAccessToken = AccessToken.getCurrentAccessToken();
            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile == null) {
                    mUserHelper.setCurrentUser(null);

                    // Logout
                    if (oldProfile != null && mUserSessionCallback != null) {
                        mUserSessionCallback.onLogout();
                    }
                } else {
                    requestUserData();
                }
            }
        };

        // If the access token is available already assign it.
        mAccessToken = AccessToken.getCurrentAccessToken();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(this.mActivity, mPermissions);
    }

    @Override
    public void onSuccess(Object o) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException error) {
    }

    public boolean isFacebookInstalled() {
        try {
            mActivity.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void onDestroy() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    public void requestUserData() {
        GraphRequest request = GraphRequest.newMeRequest(
                this.mAccessToken,
                (object, response) -> {
                    try {
                        if (response.getError() == null) {
                            User currentUser = new User();
                            currentUser.setUserId(object.getString("id")); // id from public_profile
                            currentUser.setUserName(object.getString("id")); // id from public_profile
                            currentUser.setFirstName(object.getString("first_name")); // first_name from public_profile
                            currentUser.setLastName(object.getString("last_name")); // last_name from public_profile
                            currentUser.setDisplayName(object.getString("name")); // name from public_profile
                            currentUser.setMail(object.getString("email")); // name from email
                            currentUser.setProviderDisplayName("Facebook");
                            currentUser.setProvider(PROVIDER_NAME);

                            mUserHelper.setCurrentUser(currentUser);

                            if (mUserSessionCallback != null) {
                                mUserSessionCallback.onLogin();
                            }
                        } else {
                            Log.e(YourApplication.LOG_TAG, "Error facebook graph request: " + response.getError().toString());
                        }

                    } catch (JSONException e) {
                        Log.e(YourApplication.LOG_TAG, "Error reading facebook profile", e);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void setUserSessionCallback(UserSessionCallback userSessionCallback) {
        this.mUserSessionCallback = userSessionCallback;
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }
}
