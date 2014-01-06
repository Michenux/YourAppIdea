package org.michenux.drodrolib.accounts;

import android.text.TextUtils;

public class AccountUtils {

    public static String buildAccountName(String flavor, String accountName, boolean debug) {
        StringBuilder authority = new StringBuilder();
        if ( !TextUtils.isEmpty(flavor)) {
            authority.append(flavor);
            authority.append('.');
        }
        authority.append(accountName);
        if (debug) {
            authority.append(".debug");
        }
        return authority.toString();
    }
}
