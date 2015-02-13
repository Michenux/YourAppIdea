package org.michenux.yourappidea.tutorial.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseIntArray;

import org.michenux.drodrolib.network.sync.AbstractSyncHelper;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.sync.TutorialContentProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TutorialSyncHelper extends AbstractSyncHelper {

    public static final String ACCOUNT = "tutorialaccount" ;

    @Inject
    public TutorialSyncHelper() {
        setDefaultIntervalIndex(5);
        SparseIntArray intervals = new SparseIntArray();
        if (BuildConfig.DEBUG) {
            intervals.put(1, 2); // every hour
            intervals.put(2, 2); // every two-hour
            intervals.put(3, 2); // every six-hour
            intervals.put(4, 2);// twice a day
            intervals.put(5, 2);// once a day
            intervals.put(6, 2);// every 2 days
            intervals.put(7, 2);// every 3 days
            intervals.put(8, 2);// every 4 days
        }else {
            intervals.put(1, 1 * MINUTE_PER_HOUR); // every hour
            intervals.put(2, 2 * MINUTE_PER_HOUR); // every two-hour
            intervals.put(3, 6 * MINUTE_PER_HOUR); // every six-hour
            intervals.put(4, 12* MINUTE_PER_HOUR);// twice a day
            intervals.put(5, 24* MINUTE_PER_HOUR);// once a day
            intervals.put(6, 48* MINUTE_PER_HOUR);// every 2 days
            intervals.put(7, 72* MINUTE_PER_HOUR);// every 3 days
            intervals.put(8, 96* MINUTE_PER_HOUR);// every 4 days
        }
        setIntervals(intervals);
    }

    public void createTutorialAccount( Context context ) {
        super.createAccount(ACCOUNT, context.getString(R.string.tutorials_accountType), TutorialContentProvider.AUTHORITY,
                this.isNotificationEnabled(context), context);
    }


    @Override
    public void adjustSyncInterval(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "adjustSyncInterval");
        }
        super.adjustSyncInterval(context);
    }

    @Override
    protected void increaseFrequency(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "increaseFrequency");
        }
        super.increaseFrequency(context);
    }

    @Override
    protected void reduceFrequency(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "reduceFrequency");
        }
        super.reduceFrequency(context);
    }

    @Override
    protected void saveIntervalIndex(int intervalIndex, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "saveIntervalIndex: " + intervalIndex );
        }
        super.saveIntervalIndex(intervalIndex, context);
    }

    @Override
    public boolean createAccount(String accountName, String accountType, String authority, boolean enableSync, Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "accounType: " + accountType);
        }
        boolean result = super.createAccount(accountName, accountType, authority, enableSync, context);
        if (BuildConfig.DEBUG) {
            if ( result ) {
                Log.d(YourApplication.LOG_TAG, accountName + " account created");
            }
            else {
                Log.d(YourApplication.LOG_TAG, accountName + " account already exists");
            }
        }
        return result;
    }

    @Override
    protected boolean addPeriodicSync(int intervalInMinute) {
        if ( BuildConfig.DEBUG ) {
            Log.d(YourApplication.LOG_TAG, "addPeriodicSync in minute: " + intervalInMinute);
        }
        boolean added = super.addPeriodicSync(intervalInMinute);
        if (BuildConfig.DEBUG) {
            if ( added ) {
                Log.d(YourApplication.LOG_TAG, "periodic sync does not exist");
                Log.d(YourApplication.LOG_TAG, "add periodic sync with interval in minute : " + intervalInMinute );
            }
            else {
                Log.d(YourApplication.LOG_TAG, "periodic sync already exists");
            }
        }
        return added;
    }

    public boolean isNotificationEnabled( Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("notificationPref", true);
    }
}
