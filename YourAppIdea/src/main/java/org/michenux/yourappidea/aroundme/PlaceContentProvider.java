package org.michenux.yourappidea.aroundme;

import android.content.UriMatcher;
import android.net.Uri;

import org.michenux.drodrolib.db.AbstractContentProvider;

public class PlaceContentProvider extends AbstractContentProvider {

    public static final String TABLE_NAME = "T_PLACE";
    public static final String NAME_COLUMN = "NAME";
    public static final String COUNTRY_COLUMN = "COUNTRY";
    public static final String URL_COLUMN = "URL";
    public static final String LONGITUDE_COLUMN = "LONGITUDE";
    public static final String LATITUDE_COLUMN = "LATITUDE";

    private static final String AUTHORITY = "org.michenux.yourappidea.places";
    private static final String BASE_PATH = "places";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, LIST);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
    }

    public PlaceContentProvider() {
        super(TABLE_NAME, uriMatcher, BASE_PATH );
    }
}
