package org.michenux.yourappidea.tutorial.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.michenux.yourappidea.YourApplication;

public class TutorialSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();

    private static TutorialSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(YourApplication.LOG_TAG, "tutorialSyncService.onCreate()");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new TutorialSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(YourApplication.LOG_TAG, "tutorialSyncService.onBind()");
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
