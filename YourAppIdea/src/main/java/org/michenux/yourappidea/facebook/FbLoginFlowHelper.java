package org.michenux.yourappidea.facebook;

import android.app.Activity;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.michenux.drodrolib.security.User;
import org.michenux.drodrolib.security.UserHelper;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class FbLoginFlowHelper implements Session.StatusCallback {

    private Activity mContext;

    private List<String> mPermissions ;

    private FacebookStatusCallback mStatusCallback;

    @Inject
    UserHelper mUserHelper ;

    public FbLoginFlowHelper( UserHelper userHelper ) {
        this.mUserHelper = userHelper;
    }

    // create it in activity constructor
    public FbLoginFlowHelper(UserHelper userHelper, Activity context) {
        this(userHelper, context, "basic_info" );
        this.mUserHelper = userHelper;
    }

    // create it in activity constructor
    public FbLoginFlowHelper(UserHelper userHelper, Activity context, String... permissions) {
        this.mContext = context;
        this.mPermissions = Arrays.asList(permissions);
        this.mUserHelper = userHelper;
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
            if ( mStatusCallback != null ) {
                mStatusCallback.onFacebookSessionClose();
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
                   currentUser.setUserName(user.getFirstName());
                   currentUser.setLastName(user.getLastName());
                   FbLoginFlowHelper.this.mUserHelper.setCurrentUser(currentUser);
                    if ( mStatusCallback != null) {
                        mStatusCallback.onFacebookSessionOpened();
                    }
                }
            }
        }).executeAsync();
    }


    // call this in onResume of activity
    public void onResume() {
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
    }

    public void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(mContext)
                    .setPermissions(this.mPermissions)
                    .setCallback(this));
        } else {
            Session.openActiveSession(mContext, true, this);
        }
    }

    public void setStatusCallback(FacebookStatusCallback statusCallback) {
        this.mStatusCallback = statusCallback;
    }

    public static interface FacebookStatusCallback {

        public void onFacebookSessionOpened();

        public void onFacebookSessionClose();
    }
}
