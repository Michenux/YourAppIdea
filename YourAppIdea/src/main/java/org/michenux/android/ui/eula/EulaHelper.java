package org.michenux.android.ui.eula;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EulaHelper implements EulaDialogFragment.OnEulaAgreement {

    private static final String ASSET_EULA = "EULA";
    private static final String PREFERENCES_EULA = "eula";
    private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";

    private FragmentActivity fragmentActivity;

    public EulaHelper(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public boolean showAcceptRefuse(int title, int acceptLabel, int refuseLabel) {
        boolean shown = false;
        if (!isAccepted()) {
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            String eula = readEula(fragmentActivity);
            EulaDialogFragment dialogFragment = EulaDialogFragment.newInstance(EulaDialogFragment.ACCEPTREFUSE_MODE, title, acceptLabel, refuseLabel, 0, eula, this);
            dialogFragment.show(fm, null);
            shown = true;
        }
        return shown;
    }

    public void show(int title, int closeLabel) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        String eula = readEula(fragmentActivity);
        EulaDialogFragment dialogFragment = EulaDialogFragment.newInstance(EulaDialogFragment.VIEW_MODE, title, 0, 0, closeLabel, eula, this);
        dialogFragment.show(fm, null);
    }

    @Override
    public void onAccept() {
        saveAccept();
    }

    @Override
    public void onRefuse() {
        fragmentActivity.finish();
    }

    public boolean isAccepted() {
        SharedPreferences preferences = fragmentActivity.getSharedPreferences(PREFERENCES_EULA, Activity.MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false);
    }

    public void saveAccept() {
        fragmentActivity.getSharedPreferences(PREFERENCES_EULA, Activity.MODE_PRIVATE).edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }

    private String readEula(FragmentActivity activity) {

        StringBuilder buffer = new StringBuilder();
        try {
            InputStreamReader is = new InputStreamReader(activity.getAssets().open(ASSET_EULA));
            try {
                BufferedReader reader = new BufferedReader(is);
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append('\n');
                    }
                } finally {
                    reader.close();
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            onRefuse();
        }
        return buffer.toString();
    }

    @Override
    public void onNewActivityAttached(Activity activity) {
        this.fragmentActivity = (FragmentActivity) activity;
    }

    protected FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }
}
