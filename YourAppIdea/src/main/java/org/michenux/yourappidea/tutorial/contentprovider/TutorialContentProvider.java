package org.michenux.yourappidea.tutorial.contentprovider;

import android.content.UriMatcher;
import android.net.Uri;

import org.michenux.android.db.AbstractContentProvider;

public class TutorialContentProvider extends AbstractContentProvider {

    public static final String TABLE_NAME = "T_TUTORIAL" ;
    public static final String TITLE_COLUMN = "TITLE";
    public static final String DESCRIPTION_COLUMN = "DESCRIPTION";
    public static final String URL_COLUMN = "URL";
    public static final String DATECREATION_COLUMN = "DATECREATION";

    public static final String AUTHORITY = "org.michenux.yourappidea.provider";
    private static final String BASE_PATH = "tutorial";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, LIST);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
    }

    public TutorialContentProvider() {
        super(TABLE_NAME, uriMatcher, BASE_PATH );
    }
}
