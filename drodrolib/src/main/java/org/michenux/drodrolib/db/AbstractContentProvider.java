package org.michenux.drodrolib.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.db.sqlite.SQLiteDatabaseFactory;

import javax.inject.Inject;

public abstract class AbstractContentProvider extends ContentProvider {

    public static final String ID_COLUMN = "_id";

    protected static final int LIST = 10;
    protected static final int ITEM_ID = 20;

    private UriMatcher mUriMatcher;
    private String mTableName ;
    private String mBasePath;

    @Inject
    SQLiteDatabaseFactory sqliteDatabaseFactory;

    public AbstractContentProvider( String tableName, UriMatcher uriMatcher, String basePath ) {
        super();
        this.mTableName = tableName;
        this.mUriMatcher = uriMatcher;
        this.mBasePath = basePath;
    }

    @Override
    public boolean onCreate() {
        ((MCXApplication) getContext().getApplicationContext()).inject(this);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(mTableName);

        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case LIST:
                break;
            case ITEM_ID:
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
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
        long id = 0;
        switch (uriType) {
            case LIST:
                id = sqlDB.insert(mTableName, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(mBasePath + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case LIST:
                rowsUpdated = sqlDB.update(mTableName, values, selection,
                        selectionArgs);
                break;
            case ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(mTableName, values,
                            ID_COLUMN + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(mTableName, values,
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

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = this.sqliteDatabaseFactory.getDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case LIST:
                rowsDeleted = sqlDB.delete(mTableName, selection,
                        selectionArgs);
                break;
            case ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(mTableName,
                            ID_COLUMN + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(mTableName,
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
}
