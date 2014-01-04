package org.michenux.drodrolib.ui.changelog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;

import org.michenux.drodrolib.info.VersionUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeLogHelper {

    private static final String PREFERENCE_CHANGELOG_LASTSHOWN_VERSION = "changelog.lastshown.version";

    private String cssStyle = "h1 { margin-left: 0px; font-size: 12pt; }"
            + "li { margin-left: 0px; font-size: 9pt; }"
            + "ul { padding-left: 30px; }"
            + ".summary { font-size: 9pt; color: #606060; display: block; clear: left; }"
            + ".date { font-size: 9pt; color: #606060;  display: block; }";

    public void showWhatsNew( int resTitle, int resCloseLabel, int resChangeLog, FragmentActivity fragmentActivity ) {
        int lastShownVersion = this.loadLastShownVersion(fragmentActivity);
        int appVersion = VersionUtils.getVersionCode(fragmentActivity);
        if ( lastShownVersion < appVersion ) {
            if ( lastShownVersion != 0 ) {
                this.showChangeLogFromVersion(resTitle, resCloseLabel, resChangeLog, lastShownVersion, fragmentActivity);
            }
            this.saveLastShownVersion(appVersion, fragmentActivity);
        }
    }

    public void showFullChangeLog( int resTitle, int resCloseLabel, int resChangeLog, FragmentActivity fragmentActivity ) {
        this.showChangeLogFromVersion(resTitle, resCloseLabel, resChangeLog, 0, fragmentActivity);
    }

    public void showChangeLogFromVersion( int resTitle, int resCloseLabel, int resChangeLog, int fromVersion, FragmentActivity fragmentActivity ) {
        String changeLog = getHTMLChangelog(resChangeLog, fragmentActivity.getResources(), fromVersion, fragmentActivity);
        if ( !TextUtils.isEmpty(changeLog)) {
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            ChangeLogDialogFragment dialogFragment = ChangeLogDialogFragment.newInstance(resTitle, resCloseLabel, changeLog);
            dialogFragment.show(fm, null);
        }
    }

    private String getHTMLChangelog(int resourceId, Resources resources, int fromVersion, Context context) {
        boolean releaseFound = false;
        StringBuilder changeLogBuilder = new StringBuilder("<html><head>");
        changeLogBuilder.append(getStyle()).append("</head><body>");
        XmlResourceParser xmlParser = resources.getXml(resourceId);
        try {
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && (xmlParser.getName().equals("release"))) {
                    //Check if the version matches the release tag.
                    //When version is 0 every release tag is parsed.
                    int versionCode = Integer.parseInt(xmlParser.getAttributeValue(null, "versioncode"));
                    if (versionCode > fromVersion ) {
                        parseReleaseTag(changeLogBuilder, xmlParser, context);
                        releaseFound = true; //At lease one release tag has been parsed.
                    }
                }
                eventType = xmlParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            xmlParser.close();
        }
        changeLogBuilder.append("</body></html>");

        String changeLog = null;
        if ( releaseFound) {
            changeLog = changeLogBuilder.toString();
        } else {
            changeLog = "";
        }
        return changeLog ;
    }

    private void parseReleaseTag( StringBuilder changelogBuilder, XmlPullParser resourceParser, Context context) throws XmlPullParserException, IOException {
        changelogBuilder.append("<h1>Release: ").append(resourceParser.getAttributeValue(null, "version")).append("</h1>");

        //Add date if available
        if (resourceParser.getAttributeValue(null, "date") != null) {
            changelogBuilder.append("<span class='date'>").append(parseDate(resourceParser.getAttributeValue(null, "date"), context)).append("</span>");
        }

        //Add summary if available
        if (resourceParser.getAttributeValue(null, "summary") != null) {
            changelogBuilder.append("<span class='summary'>").append(resourceParser.getAttributeValue(null, "summary")).append("</span>");
        }

        changelogBuilder.append("<ul>");

        //Parse child nodes
        int eventType = resourceParser.getEventType();
        while ((eventType != XmlPullParser.END_TAG) || (resourceParser.getName().equals("change"))) {
            if ((eventType == XmlPullParser.START_TAG) && (resourceParser.getName().equals("change"))) {
                eventType = resourceParser.next();
                changelogBuilder.append("<li>").append(resourceParser.getText()).append("</li>");
            }
            eventType = resourceParser.next();
        }
        changelogBuilder.append("</ul>");
    }

    //Parse a date string from the xml and format it using the local date format
    private String parseDate(final String dateString, Context context) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            final Date parsedDate = dateFormat.parse(dateString);
            return DateFormat.getDateFormat(context).format(parsedDate);
        } catch (ParseException ignored) {
            //If there is a problem parsing the date just return the original string
            return dateString;
        }
    }

    private String getStyle() {
        return String.format("<style type=\"text/css\">%s</style>", cssStyle);
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }


    private int loadLastShownVersion(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREFERENCE_CHANGELOG_LASTSHOWN_VERSION, 0);
    }

    public void saveCurrentVersion( Context context ) {
        int appVersion = VersionUtils.getVersionCode(context);
        saveLastShownVersion(appVersion, context);
    }

    private void saveLastShownVersion( int version, Context context ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(PREFERENCE_CHANGELOG_LASTSHOWN_VERSION, version).commit();
    }
}
