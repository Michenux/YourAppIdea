package org.michenux.yourappidea.friends;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.michenux.android.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.yourappidea.YourApplication;

import javax.inject.Inject;

/**
 * @author Michenux
 * 
 */
public class FriendContentProvider extends ContentProvider {

    public static final String TABLE_NAME = "T_FRIEND" ;
    public static final String ID_COLUMN = "_ID";
    public static final String NAME_COLUMN = "NAME";
    public static final String JOB_COLUMN = "JOB";
    public static final String FACE_COLUMN = "FACE";

	/**
	 * 
	 */
	private static final int FRIENDS = 10;

	/**
	 * 
	 */
	private static final int FRIEND_ID = 20;

	/**
	 * 
	 */
	private static final String AUTHORITY = "org.michenux.yourappidea.friends";

	/**
	 * 
	 */
	private static final String BASE_PATH = "friends";

	/**
	 * 
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	/**
	 * 
	 */
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/friends";

	/**
	 * 
	 */
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/friend";

	/**
	 * 
	 */
	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, BASE_PATH, FRIENDS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FRIEND_ID);
	}
	
	@Inject SQLiteDatabaseFactory sqliteDatabaseFactory ;
	
	@Override
	public boolean onCreate() {
		
		((YourApplication) getContext().getApplicationContext()).inject(this);
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = uriMatcher.match(uri);
		SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case FRIENDS:
			rowsDeleted = sqlDB.delete(TABLE_NAME, selection,
					selectionArgs);
			break;
		case FRIEND_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(TABLE_NAME,
                        ID_COLUMN + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(TABLE_NAME,
                        ID_COLUMN + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = uriMatcher.match(uri);
		SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
		long id = 0;
		switch (uriType) {
		case FRIENDS:
			id = sqlDB.insert(TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TABLE_NAME);

		int uriType = uriMatcher.match(uri);
		switch (uriType) {
		case FRIENDS:
			break;
		case FRIEND_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(ID_COLUMN + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(sqlDB, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = uriMatcher.match(uri);
		SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case FRIENDS:
			rowsUpdated = sqlDB.update(TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case FRIEND_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TABLE_NAME, values,
                        ID_COLUMN + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(TABLE_NAME, values,
                        ID_COLUMN + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
}
