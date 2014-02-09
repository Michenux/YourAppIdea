package org.michenux.drodrolib.accounts;

import android.text.TextUtils;

public class AccountUtils {

    public static String buildAccountTypeName(String flavor, String accountTypeName, boolean debug) {
        StringBuilder authority = new StringBuilder();
        if ( !TextUtils.isEmpty(flavor)) {
            authority.append(flavor);
            authority.append('.');
        }
        authority.append(accountTypeName);
        if (debug) {
            authority.append(".debug");
        }
        return authority.toString();
    }
}
