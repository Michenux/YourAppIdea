package org.michenux.android.db.utils;

import android.database.Cursor;

/**
 * @author Michenux
 *
 */
public class CursorUtils {

	public static String getString( String columnName, Cursor cursor ) {
		return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}

    public static int getInt( String columnName, Cursor cursor ) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getLong( String columnName, Cursor cursor ) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }
}
