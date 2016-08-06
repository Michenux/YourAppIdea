package org.michenux.yourappidea.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.michenux.drodrolib.battery.BatteryUtils;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.YourApplication;

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "PowerConnectionReceiver");
            Log.d(YourApplication.LOG_TAG, "  isChargingOrFull: " + BatteryUtils.isChargingOrFull(context));
            Log.d(YourApplication.LOG_TAG, "  charging mode: " + BatteryUtils.getChargingMode(context));
            Log.d(YourApplication.LOG_TAG, "  charge pct: " + BatteryUtils.getChargePct(context));
        }
    }
}
