package org.michenux.yourappidea.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.battery.BatteryUtils;
import org.michenux.yourappidea.BuildConfig;

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, "PowerConnectionReceiver");
            Log.d(MCXApplication.LOG_TAG, "  isChargingOrFull: " + BatteryUtils.isChargingOrFull(context));
            Log.d(MCXApplication.LOG_TAG, "  charging mode: " + BatteryUtils.getChargingMode(context));
            Log.d(MCXApplication.LOG_TAG, "  charge pct: " + BatteryUtils.getChargePct(context));
        }
    }
}
