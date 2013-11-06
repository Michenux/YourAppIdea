package org.michenux.yourappidea.tutorial.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.YourApplication;

public class TutorialAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private TutorialAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        if (BuildConfig.DEBUG ) {
            Log.d(YourApplication.LOG_TAG, "TutorialAuthenticatorService.onCreate()");
        }
        mAuthenticator = new TutorialAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG ) {
            Log.d(YourApplication.LOG_TAG, "TutorialAuthenticatorService.onBind()");
        }
        return mAuthenticator.getIBinder();
    }
}
