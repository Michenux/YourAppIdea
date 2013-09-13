package org.michenux.yourappidea.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import org.michenux.android.ui.preferences.PreferenceCompatFragment;

public class SettingsFragment extends PreferenceCompatFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceCompatFragment.OnPreferenceAttachedListener {

    public static final String SHARED_PREFS_NAME = "settings";

    public static SettingsFragment newInstance(int preferences) {
        SettingsFragment prefFragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt("xml", preferences);
        prefFragment.setArguments(args);
        return prefFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);
        preferenceManager.getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if ( key.equals("notificationPref")) {
            //TODO:
        }
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
        if (root == null)
            return; // can be null sometimes
    }
}
