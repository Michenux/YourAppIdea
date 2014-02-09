package org.michenux.drodrolib.network.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.PeriodicSync;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;

import org.michenux.drodrolib.info.AppUsageUtils;

import java.util.List;

public abstract class AbstractSyncHelper {

    private static final String SYNCINTERVAL_PARAM = "syncInterval";
    public static final int MINUTE_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;

    private Account mAccount ;

    private String mAuthority;

    private int mDefaultIntervalIndex;

    private SparseIntArray mIntervals ;

    /**
     * Adjust synchronization interval
     * @param context context
     */
    public void adjustSyncInterval(Context context ) {
        long lastSync = AppUsageUtils.getLastSync(context);
        long appLastUsed = AppUsageUtils.getLastUsedTimestamp(context);

        // if app used since last sync, increase frequency
        if ( appLastUsed > lastSync ) {
            increaseFrequency(context);
        }
        // if app not used since last sync, reduce frequency
        else {
            reduceFrequency(context);
        }
    }

    private int getSyncIntervalIndex( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(SYNCINTERVAL_PARAM, mDefaultIntervalIndex);
    }

    private int getSyncIntervalInMinute( Context context ) {
        return getSyncIntervalInMinute(getSyncIntervalIndex(context), context);
    }

    private int getSyncIntervalInMinute( int intervalIndex, Context context ) {
        return mIntervals.get(intervalIndex);
    }

    protected void increaseFrequency(Context context) {
        int syncIntervalIndex = getSyncIntervalIndex(context);
        if ( syncIntervalIndex > 1) {
            saveIntervalIndex(--syncIntervalIndex, context);
            addPeriodicSync(getSyncIntervalInMinute(syncIntervalIndex, context));
        }
    }

    protected void reduceFrequency(Context context) {
        int syncIntervalIndex = getSyncIntervalIndex(context);
        if ( syncIntervalIndex < mIntervals.size()) {
            saveIntervalIndex(++syncIntervalIndex, context);
            addPeriodicSync(getSyncIntervalInMinute(syncIntervalIndex, context));
        }
    }

    protected void saveIntervalIndex( int intervalIndex, Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SYNCINTERVAL_PARAM, intervalIndex);
        editor.commit();
    }


    public boolean createAccount( String accountName, String accountType, String authority, boolean enableSync, Context context ) {

        this.mAccount = new Account(accountName, accountType);
        this.mAuthority = authority ;
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        boolean result = accountManager.addAccountExplicitly(mAccount, null, null);

        if ( enableSync ) {
            addPeriodicSync(getSyncIntervalInMinute(context));
        }

        AppUsageUtils.updateLastSync(context);

        return result;
    }

    /**
     * Remove any periodic sync for this account
     */
    public void removePeriodicSync() {
        Bundle bundle = new Bundle();
        ContentResolver.removePeriodicSync(mAccount, mAuthority, bundle);
    }

    public void enablePeriodicSync( Context context) {
        addPeriodicSync(getSyncIntervalInMinute(context));
    }

    /**
     * Schedule syncAdapter by interval
     * @param intervalInMinute interval in minute
     */
    protected boolean addPeriodicSync( int intervalInMinute ) {
        boolean added = false;
        if ( !periodicSyncExists(intervalInMinute )) {
              Bundle bundle = new Bundle();

            // remove all others
            removePeriodicSync();

            ContentResolver.setIsSyncable(mAccount, mAuthority, 1);
            ContentResolver.setSyncAutomatically(mAccount, mAuthority, true);


            long syncInSecond = intervalInMinute * SECONDS_PER_MINUTE;
            ContentResolver.addPeriodicSync(
                    mAccount,
                    mAuthority,
                    bundle,
                    syncInSecond);

            added = true;
        }
        return added;
    }

    /**
     * Test if periodic sync is already configured
     * @param intervalInMinute interval in minute
     * @return true if already exists
     */
    private boolean periodicSyncExists( int intervalInMinute ) {
        boolean exists = false;
        long syncInSecond = intervalInMinute * SECONDS_PER_MINUTE;
        List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(mAccount, mAuthority);
        if ( periodicSyncs != null && !periodicSyncs.isEmpty()) {
            for( PeriodicSync periodicSync : periodicSyncs ) {
                if (periodicSync.period == syncInSecond) {
                    exists = true ;
                }
            }
        }
        return exists;
    }

    public void performSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, mAuthority, settingsBundle);
    }

    public boolean isActiveOrPending() {
        return ContentResolver.isSyncActive(mAccount, mAuthority) ||
                ContentResolver.isSyncPending(mAccount, mAuthority);
    }

    public void setDefaultIntervalIndex(int defaultIntervalIndex) {
        this.mDefaultIntervalIndex = defaultIntervalIndex;
    }

    public void setIntervals(SparseIntArray intervals) {
        this.mIntervals = intervals;
    }
}
