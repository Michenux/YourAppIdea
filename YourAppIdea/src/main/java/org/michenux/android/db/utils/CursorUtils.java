package org.michenux.android.db.utils;

import android.database.Cursor;

/**
 * @author Michenux
 *
 */
public class CursorUtils {

	/**
	 * @param columnName
	 * @param cursor
	 * @return
	 */
	public static String getString( String columnName, Cursor cursor ) {
		return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}
}
