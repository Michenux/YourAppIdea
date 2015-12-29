package org.michenux.yourappidea.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.widget.LoginButton;

import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import javax.inject.Inject;

public class FbLoginFragment extends Fragment implements UserSessionCallback {

    @Inject
    UserHelper mUserHelper;

    private FacebookDelegate mFacebookDelegate ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);

        mFacebookDelegate = new FacebookDelegate(mUserHelper, this.getActivity());
        mFacebookDelegate.setUserSessionCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_facebook, container, false);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.navmenufacebook_loginbutton);
        loginButton.setReadPermissions("public_profile", "email");
        loginButton.setFragment(this);

        // Hide facebook login button if facebook app not installed
        if ( !mFacebookDelegate.isFacebookInstalled()) {
            loginButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        mFacebookDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLogin() {
        if ( this.getActivity() != null ) {
            this.getActivity().finish();
        }
    }

    @Override
    public void onLogout() {

    }
}
