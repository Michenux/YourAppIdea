package org.michenux.drodrolib.ui.eula;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.michenux.drodrolib.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EulaHelper {
    private static final String ASSET_EULA = "EULA";
    private static final String PREFERENCES_EULA = "eula";
    private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";

    public static boolean showAcceptRefuse(final FragmentActivity activity, int title, int acceptLabel, int refuseLabel) {
        boolean shown = false;
        if (!isAccepted(activity)) {
            String eula = readEula(activity);

            new MaterialDialog.Builder(activity)
                    .title(title)
                    .content(eula)
                    .negativeText(refuseLabel)
                    .positiveText(acceptLabel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            activity.finish();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            saveAccept(activity);
                        }
                    })
                    .show();
            shown = true;
        }
        return shown;
    }

    public static void show(FragmentActivity activity, int title) {
        String eula = readEula(activity);
        new MaterialDialog.Builder(activity)
                .title(title)
                .content(eula)
                .positiveText(R.string.close)
                .show();
    }

    public static boolean isAccepted(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_EULA, Activity.MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false);
    }

    public static void saveAccept(FragmentActivity activity) {
        activity.getSharedPreferences(PREFERENCES_EULA, Activity.MODE_PRIVATE).edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }

    private static String readEula(FragmentActivity activity) {
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
            activity.finish();
        }
        return buffer.toString();
    }
}
