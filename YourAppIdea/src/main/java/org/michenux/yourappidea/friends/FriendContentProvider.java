package org.michenux.yourappidea.friends;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import org.michenux.drodrolib.content.ContentProviderUtils;
import org.michenux.drodrolib.db.AbstractContentProvider;
import org.michenux.yourappidea.BuildConfig;

public class FriendContentProvider extends AbstractContentProvider {

    public static final String TABLE_NAME = "T_FRIEND" ;
    public static final String NAME_COLUMN = "NAME";
    public static final String JOB_COLUMN = "JOB";
    public static final String FACE_COLUMN = "FACE";

	private static final String AUTHORITY = ContentProviderUtils.buildAuthority(
            BuildConfig.APPLICATION_ID, BuildConfig.FLAVOR, "friends");

	private static final String BASE_PATH = "friends";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/friends";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/friend";

	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, LIST);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
	}

    public FriendContentProvider() {
        super(TABLE_NAME, uriMatcher, BASE_PATH );
    }
}
