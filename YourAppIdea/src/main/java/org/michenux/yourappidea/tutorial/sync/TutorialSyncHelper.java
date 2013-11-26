package org.michenux.yourappidea.tutorial.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.PeriodicSync;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.contentprovider.TutorialContentProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TutorialSyncHelper {

    public static final String ACCOUNT_TYPE = "michenux.net";
    public static final String ACCOUNT = "tutorialaccount";

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1440L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE ;

    private Account tutorialAccount;

    @Inject
    public TutorialSyncHelper() {

    }

    public void createTutorialAccount( Context context ) {
        this.tutorialAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(tutorialAccount, null, null)) {
            if (BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "tutorial account created");
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "tutorial account already exists");
            }
        }

        if ( this.isNotificationEnabled(context)) {
            this.addPeriodicSync();
        }

        updateLastTutoSync( context );
    }

    public long getLastTutoSync( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong("lastTutoSync", 0 );
    }

    public void updateLastTutoSync( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long lastTutoSync = prefs.getLong( "lastTutoSync", 0 );
        long now = System.currentTimeMillis();
        if ( lastTutoSync < now) {
            if (BuildConfig.DEBUG) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(now));
                cal.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                Log.d(YourApplication.LOG_TAG, "set new last sync date to : " + sdf.format(cal.getTime()));
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("lastTutoSync", now);
            editor.commit();
        }
    }

    public boolean isNotificationEnabled( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("notificationPref", true);
    }

    public void addPeriodicSync() {
        if ( !periodicSyncExists(SYNC_INTERVAL)) {

            if ( BuildConfig.DEBUG ) {
                Log.d(YourApplication.LOG_TAG, "periodic sync does not exist");
            }

            Bundle bundle = new Bundle();

            // remove all others
            removePeriodicSync();

            ContentResolver.setIsSyncable(tutorialAccount, TutorialContentProvider.AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(tutorialAccount, TutorialContentProvider.AUTHORITY, true);

            // add the right one
            if ( BuildConfig.DEBUG ) {
                Log.d(YourApplication.LOG_TAG, "add periodic sync with period: " + SYNC_INTERVAL );
            }

            ContentResolver.addPeriodicSync(
                    tutorialAccount,
                    TutorialContentProvider.AUTHORITY,
                    bundle,
                    SYNC_INTERVAL);
        }
        else if ( BuildConfig.DEBUG ) {
            Log.d(YourApplication.LOG_TAG, "periodic sync already exists");
        }
    }

    public void performSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(tutorialAccount, TutorialContentProvider.AUTHORITY, settingsBundle);
    }

    public void removePeriodicSync() {
        Bundle bundle = new Bundle();
        ContentResolver.removePeriodicSync(tutorialAccount, TutorialContentProvider.AUTHORITY, bundle );
    }

    public boolean periodicSyncExists( long period ) {
        boolean exists = false;
        List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(tutorialAccount, TutorialContentProvider.AUTHORITY);
        if ( periodicSyncs != null && !periodicSyncs.isEmpty()) {
            for( PeriodicSync periodicSync : periodicSyncs ) {
                if (periodicSync.period == period) {
                    exists = true ;
                }
            }
        }
        return exists;
    }


    public boolean isActiveOrPending() {
        return ContentResolver.isSyncActive(tutorialAccount, TutorialContentProvider.AUTHORITY) ||
                ContentResolver.isSyncPending(tutorialAccount, TutorialContentProvider.AUTHORITY);
    }
}
