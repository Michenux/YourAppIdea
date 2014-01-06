package org.michenux.drodrolib.content;

import android.text.TextUtils;

public class ContentProviderUtils {

    public static String buildAuthority( String packageName, String flavor, String name) {
        StringBuilder authority = new StringBuilder(packageName);
        if ( !TextUtils.isEmpty(flavor)) {
            authority.append('.');
            authority.append(flavor);
        }
        authority.append('.');
        authority.append(name);
        return authority.toString();
    }
}
