package org.michenux.yourappidea.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.content.ReceiverUtils;
import org.michenux.drodrolib.network.connectivity.ConnectivityUtils;
import org.michenux.yourappidea.BuildConfig;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, "ConnectivityChangeReceiver.onReceive");
            Log.d(MCXApplication.LOG_TAG, "  isConnected: " + ConnectivityUtils.isConnected(context));
            Log.d(MCXApplication.LOG_TAG, "  isWifiConnected: " + ConnectivityUtils.isConnectedWifi(context));
        }

        if ( ConnectivityUtils.isConnected(context)) {
            // When connected, stop listening for changes of connectivity because consumes resource
            // When refreshing, test if connected, if not, enable the connectivty change listener
            ReceiverUtils.disableReceiver(this.getClass(), context);
        }
    }
}
