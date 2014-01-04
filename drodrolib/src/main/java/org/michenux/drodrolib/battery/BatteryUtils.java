package org.michenux.drodrolib.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryUtils {

    public static boolean isChargingOrFull( Context context ) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        return isChargingOrFull(batteryStatus);
    }

    public static boolean isChargingOrFull(Intent batteryStatus) {
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     *
     * @param context
     * @return BatteryManager.BATTERY_PLUGGED_USB, BatteryManager.BATTERY_PLUGGED_AC, BatteryManager.BATTERY_PLUGGED_WIRELESS
     */
    public static int getChargingMode( Context context ) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        return getChargingMode(batteryStatus);
    }

    public static int getChargingMode(Intent batteryStatus) {
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    }

    public static float getChargePct( Context context ) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        return getChargePct(batteryStatus);
    }

    public static float getChargePct(Intent batteryStatus) {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return level / (float) scale ;
    }
}
