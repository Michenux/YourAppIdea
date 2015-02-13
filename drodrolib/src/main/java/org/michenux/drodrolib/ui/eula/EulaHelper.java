package org.michenux.drodrolib.ui.eula;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;

import org.michenux.drodrolib.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EulaHelper {

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

            new MaterialDialog.Builder(this.fragmentActivity)
                    .title(title)
                    .content(eula)
                    .negativeText(refuseLabel)
                    .positiveText(acceptLabel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            saveAccept();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            fragmentActivity.finish();
                        }
                    })
                    .show();
            shown = true;
        }
        return shown;
    }

    public void show(int title, int closeLabel) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        String eula = readEula(fragmentActivity);
        new MaterialDialog.Builder(this.fragmentActivity)
                .title(title)
                .content(eula)
                .positiveText(R.string.close)
                .show();
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
            fragmentActivity.finish();
        }
        return buffer.toString();
    }

    protected FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }
}
