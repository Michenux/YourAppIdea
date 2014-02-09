package org.michenux.drodrolib.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
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

    public static int count(Uri uri,String selection,String[] selectionArgs, ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(uri,new String[] {"count(*)"},
                selection, selectionArgs, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return 0;
        } else {
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
    }
}
