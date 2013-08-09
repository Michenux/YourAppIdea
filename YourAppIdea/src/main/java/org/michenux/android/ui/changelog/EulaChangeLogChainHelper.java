package org.michenux.android.ui.changelog;

import android.support.v4.app.FragmentActivity;

import org.michenux.android.ui.eula.EulaHelper;

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
        showAcceptRefuse(this.eulaTitle, this.eulaAcceptLabel, this.eulaRefuseLabel);
    }

    @Override
    public boolean showAcceptRefuse(int title, int acceptLabel, int refuseLabel) {
        boolean shown = super.showAcceptRefuse(title, acceptLabel, refuseLabel);

        if (!shown) {
            showChangeLog();
        }

        return shown;
    }

    @Override
    public void onAccept() {
        super.onAccept();
        showChangeLog();
    }

    protected void showChangeLog() {
        ChangeLogHelper changeLogHelper = new ChangeLogHelper();
        changeLogHelper.showWhatsNew(changeLogTitle, changeLogClose, changeLog, getFragmentActivity());
    }

}
