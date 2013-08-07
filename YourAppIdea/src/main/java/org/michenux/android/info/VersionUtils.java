package org.michenux.android.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author Michenux
 *
 */
public class VersionUtils {

	/**
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static int getVersionCode( Context context ) throws NameNotFoundException {
		PackageInfo manager= context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);
	    return manager.versionCode;
	}
}
