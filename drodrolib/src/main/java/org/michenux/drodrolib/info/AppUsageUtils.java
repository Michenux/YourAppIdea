package org.michenux.drodrolib.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppUsageUtils {

    private static final String LASTUSED_PARAM = "lastUsed";
    private static final String LASTSYNC_PARAM = "lastSync";

    public static void updateLastUsedTimestamp( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(LASTUSED_PARAM, System.currentTimeMillis());
        edit.commit();
    }

    public static long getLastUsedTimestamp( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(LASTUSED_PARAM, 0);
    }

    public static long getLastSync( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(LASTSYNC_PARAM, 0 );
    }

    public static void updateLastSync( Context context ) {
        long lastSync = getLastSync(context);
        long now = System.currentTimeMillis();
        if ( lastSync < now) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(LASTSYNC_PARAM, now);
            editor.commit();
        }
    }
}
