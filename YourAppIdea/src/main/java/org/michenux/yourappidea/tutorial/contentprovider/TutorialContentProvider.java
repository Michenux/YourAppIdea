package org.michenux.yourappidea.tutorial.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import org.michenux.android.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.yourappidea.YourApplication;

import javax.inject.Inject;

public class TutorialContentProvider extends ContentProvider {

    public static final String AUTHORITY = "org.michenux.yourappidea.provider";

    private static final int WATCHS = 10;

    private static final int WATCH_ID = 20;

    private static final String BASE_PATH = "tutorial";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/tutorials";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/tutorial";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, WATCHS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WATCH_ID);
    }

    @Inject SQLiteDatabaseFactory sqliteDatabaseFactory ;

    @Override
    public boolean onCreate() {
        ((YourApplication) getContext().getApplicationContext()).inject(this);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return new String();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

//        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
//
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(WatchTable.NAME);
//
//        int uriType = uriMatcher.match(uri);
//        switch (uriType) {
//            case WATCHS:
//                break;
//            case WATCH_ID:
//                queryBuilder.appendWhere(WatchTable.Fields._ID + "=" + uri.getLastPathSegment());
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        Cursor cursor = queryBuilder.query(sqlDB, projection, selection,
//                selectionArgs, null, null, sortOrder);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//
//        return cursor;
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        int uriType = uriMatcher.match(uri);
//        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
//        long id = 0;
//        switch (uriType) {
//            case WATCHS:
//                id = sqlDB.insert(WatchTable.NAME, null, values);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return Uri.parse(BASE_PATH + "/" + id);
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
//        int uriType = uriMatcher.match(uri);
//        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
//        int rowsUpdated = 0;
//        switch (uriType) {
//            case WATCHS:
//                rowsUpdated = sqlDB.update(WatchTable.NAME, values, selection,
//                        selectionArgs);
//                break;
//            case WATCH_ID:
//                String id = uri.getLastPathSegment();
//                if (TextUtils.isEmpty(selection)) {
//                    rowsUpdated = sqlDB.update(WatchTable.NAME, values,
//                            WatchTable.Fields._ID + "=" + id, null);
//                } else {
//                    rowsUpdated = sqlDB.update(WatchTable.NAME, values,
//                            WatchTable.Fields._ID + "=" + id + " and " + selection,
//                            selectionArgs);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsUpdated;
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int uriType = uriMatcher.match(uri);
//        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
//        int rowsDeleted = 0;
//        switch (uriType) {
//            case WATCHS:
//                rowsDeleted = sqlDB.delete(WatchTable.NAME, selection,
//                        selectionArgs);
//                break;
//            case WATCH_ID:
//                String id = uri.getLastPathSegment();
//                if (TextUtils.isEmpty(selection)) {
//                    rowsDeleted = sqlDB.delete(WatchTable.NAME,
//                            WatchTable.Fields._ID + "=" + id, null);
//                } else {
//                    rowsDeleted = sqlDB.delete(WatchTable.NAME,
//                            WatchTable.Fields._ID + "=" + id + " and " + selection,
//                            selectionArgs);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsDeleted;
        return 0;
    }
}
