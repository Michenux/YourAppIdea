package org.michenux.yourappidea.activity;

import org.michenux.yourappidea.R;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 11) {
			addL11PreferencesFromResources();
		} else {
			addLegacyPreferencesFromResource();
		}

	}

	//@SuppressLint("NewApi")
	public void addLegacyPreferencesFromResource() {
		addPreferencesFromResource(R.xml.preferences);
	}

	public void addL11PreferencesFromResources() {
        addPreferencesFromResource(R.xml.preferences);
		//getFragmentManager().beginTransaction()
		//		.replace(android.R.id.content, new PrefsFragment()).commit();
	}
}
