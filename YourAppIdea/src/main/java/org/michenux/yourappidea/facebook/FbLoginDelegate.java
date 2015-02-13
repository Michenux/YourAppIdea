package org.michenux.yourappidea.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class FbLoginDelegate implements Session.StatusCallback {

    public static final String PROVIDER_NAME = "facebook";

    private Activity mContext;

    private List<String> mPermissions ;

    private UserSessionCallback mUserSessionCallback;

    private UiLifecycleHelper mUiHelper;

    @Inject
    UserHelper mUserHelper ;

    public FbLoginDelegate(UserHelper userHelper) {
        this.mUserHelper = userHelper;
    }

    // create it in activity constructor
    public FbLoginDelegate(UserHelper userHelper, Activity activity, Bundle savedInstanceState) {
        this(userHelper, activity, savedInstanceState, "basic_info", "email" );
    }

    // create it in activity constructor
    public FbLoginDelegate(UserHelper userHelper, Activity activity, Bundle savedInstanceState, String... permissions) {
        this.mContext = activity;
        this.mPermissions = Arrays.asList(permissions);
        this.mUserHelper = userHelper;

        mUiHelper = new UiLifecycleHelper(activity, this);
        mUiHelper.onCreate(savedInstanceState);
    }

    // call this in onResume of activity
    public void onResume() {
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        mUiHelper.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onPause() {
        mUiHelper.onPause();
    }

    public void onSaveInstanceState(Bundle outState) {
        mUiHelper.onSaveInstanceState(outState);
    }

    public void onStop() {
        mUiHelper.onStop();
    }

    public void onDestroy() {
        mUiHelper.onDestroy();
    }

    public boolean isFacebookInstalled() {
        try{
            ApplicationInfo info = mContext.getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

    @Override
    public void call(Session session, SessionState state, Exception exception) {
        onSessionStateChange(session, state, exception);
    }

    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            requestUserData(session);
        } else if (state.isClosed()) {
            mUserHelper.setCurrentUser(null);
            if ( mUserSessionCallback != null ) {
                mUserSessionCallback.onLogout();
            }
        }
    }

    public void requestUserData( Session session ) {
        // Request user data and show the results
        Request.newMeRequest(session, new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                   User currentUser = new User();
                    currentUser.setUserId(user.getId());
                    currentUser.setUserName(user.getUsername());
                    currentUser.setFirstName(user.getFirstName());
                    currentUser.setLastName(user.getLastName());
                    currentUser.setDisplayName(user.getName());
                    currentUser.setMail((String) user.getProperty("email"));
                    currentUser.setProviderDisplayName("Facebook");
                    currentUser.setProvider(PROVIDER_NAME);
                   FbLoginDelegate.this.mUserHelper.setCurrentUser(currentUser);
                    if ( mUserSessionCallback != null) {
                        mUserSessionCallback.onLogin();
                    }
                }
            }
        }).executeAsync();
    }




    public void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            Session.OpenRequest openRequest = new Session.OpenRequest(mContext);
            openRequest.setPermissions(this.mPermissions);
            openRequest.setCallback(this);
            session.openForRead(openRequest);
        } else {
            Session.openActiveSession(mContext, true, this);
        }
    }

    public void setUserSessionCallback(UserSessionCallback userSessionCallback) {
        this.mUserSessionCallback = userSessionCallback;
    }

    public void logout() {
        Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation();
    }
}
