package org.michenux.drodrolib.ui.changelog;

import android.support.v4.app.FragmentActivity;

import org.michenux.drodrolib.ui.eula.EulaHelper;

public class EulaChangeLogChainHelper extends EulaHelper {

    private int eulaTitle;
    private int eulaAcceptLabel;
    private int eulaRefuseLabel;

    private int changeLogTitle;
    private int changeLogClose;
    private int changeLog;

    public EulaChangeLogChainHelper(int resEulaTitle, int resEulaAcceptLabel, int resEulaRefuseLabel,
                                    int resChangeLogTitle, int resChangeLogClose, int resChangeLog,
                                    FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.eulaTitle = resEulaTitle;
        this.eulaAcceptLabel = resEulaAcceptLabel;
        this.eulaRefuseLabel = resEulaRefuseLabel;
        this.changeLogTitle = resChangeLogTitle;
        this.changeLogClose = resChangeLogClose;
        this.changeLog = resChangeLog;
    }

    public void show() {
        //not shown = already accepted
        boolean shown = showAcceptRefuse(this.eulaTitle, this.eulaAcceptLabel, this.eulaRefuseLabel);

        ChangeLogHelper changeLogHelper = new ChangeLogHelper();
        if (!shown) {
            changeLogHelper.showWhatsNew(changeLogTitle, changeLogClose, changeLog, getFragmentActivity());
        }
        else {
            //We don't show the changelog at first run of the first install, but we have to save the current version
            //for the future upgrades
            changeLogHelper.saveCurrentVersion(getFragmentActivity());
        }
    }
}
