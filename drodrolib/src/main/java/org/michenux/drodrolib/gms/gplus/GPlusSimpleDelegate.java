package org.michenux.drodrolib.gms.gplus;


import android.app.Activity;

import org.michenux.drodrolib.security.UserHelper;

public class GPlusSimpleDelegate extends GPlusDelegate {

    public GPlusSimpleDelegate(Activity activity, UserHelper userHelper) {
        super(activity, userHelper);
    }

    @Override
    protected void onPlusClientRevokeAccess() {

    }

    @Override
    protected void onPlusClientSignIn() {

    }

    @Override
    protected void onPlusClientSignOut() {

    }

    @Override
    protected void onPlusClientBlockingUI(boolean show) {

    }

    @Override
    protected void updateConnectButtonState() {

    }
}
