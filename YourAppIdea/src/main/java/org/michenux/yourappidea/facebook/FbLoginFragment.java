package org.michenux.yourappidea.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;

import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import javax.inject.Inject;

public class FbLoginFragment extends Fragment implements UserSessionCallback {

    @Inject
    UserHelper mUserHelper;

    private FbLoginDelegate mFbLoginFlowHelper ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);

        mFbLoginFlowHelper = new FbLoginDelegate(mUserHelper, this.getActivity(), savedInstanceState);
        mFbLoginFlowHelper.setUserSessionCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_facebook, container, false);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.navmenufacebook_loginbutton);
        loginButton.setReadPermissions("basic_info", "email");
        loginButton.setFragment(this);

        // Hide facebook login button if facebook app not installed
        if ( !mFbLoginFlowHelper.isFacebookInstalled()) {
            loginButton.setVisibility(View.GONE);
            //TODO: should display a message
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFbLoginFlowHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFbLoginFlowHelper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFbLoginFlowHelper.onResume();
    }

    @Override
    public void onStop() {
        mFbLoginFlowHelper.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mFbLoginFlowHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFbLoginFlowHelper.onSaveInstanceState(outState);
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
