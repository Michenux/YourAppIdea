package org.michenux.drodrolib.ui.changelog;

import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.support.v4.app.FragmentActivity;

import org.michenux.drodrolib.ui.eula.EulaHelper;

public class EulaChangeLogChainHelper {

    public static void show( FragmentActivity fragmentActivity,
                      @StringRes int resEulaTitle,
                      @StringRes int resEulaAcceptLabel,
                      @StringRes int resEulaRefuseLabel,
                      @StringRes int resChangeLogTitle,
                      @StringRes int resChangeLogClose,
                      @XmlRes int resChangeLog ) {
        //not shown = already accepted
        boolean shown = EulaHelper.showAcceptRefuse(fragmentActivity, resEulaTitle, resEulaAcceptLabel, resEulaRefuseLabel);

        ChangeLogHelper changeLogHelper = new ChangeLogHelper();
        if (!shown) {
            changeLogHelper.showWhatsNew(resChangeLogTitle, resChangeLogClose, resChangeLog, fragmentActivity);
        }
        else {
            //We don't show the changelog at first run of the first install, but we have to save the current version
            //for the future upgrades
            changeLogHelper.saveCurrentVersion(fragmentActivity);
        }
    }
}
