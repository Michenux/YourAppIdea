package org.michenux.drodrolib.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class ReceiverUtils {

    public static void enableReceiver( Class<? extends BroadcastReceiver> receiverClass, Context context ) {

        ComponentName componentName = new ComponentName(context, receiverClass);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void disableReceiver( Class<? extends BroadcastReceiver> receiverClass, Context context ) {

        ComponentName componentName = new ComponentName(context, receiverClass);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
