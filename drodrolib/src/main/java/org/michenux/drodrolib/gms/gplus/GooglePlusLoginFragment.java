package org.michenux.drodrolib.gms.gplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.SignInButton;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.R;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.security.UserSessionCallback;

import javax.inject.Inject;

public class GooglePlusLoginFragment extends Fragment implements UserSessionCallback, View.OnClickListener {

    @Inject
    UserHelper mUserHelper;

    private GoogleApiClientDelegate mGoogleApiClientDelegate;

    private SignInButton mSignInButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MCXApplication) getActivity().getApplication()).inject(this);

        mGoogleApiClientDelegate = new GoogleApiClientDelegate(this.getActivity(), mUserHelper, savedInstanceState);
        mGoogleApiClientDelegate.setUserSessionCallback(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_googleplus, container, false);
        mSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleApiClientDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGoogleApiClientDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClientDelegate.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClientDelegate.onStop();
        super.onStop();
    }

    @Override
    public void onLogin() {
        updateButtons(true);
        this.getActivity().finish();
    }

    @Override
    public void onLogout() {
        updateButtons(false);
    }

    private void updateButtons(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.INVISIBLE);
        } else {
            if ( !mGoogleApiClientDelegate.isConnectionResult()) {
                mSignInButton.setVisibility(View.INVISIBLE);
            } else {
                mSignInButton.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.sign_in_button ) {
            mGoogleApiClientDelegate.signIn(this.getActivity());
        }
    }
}
