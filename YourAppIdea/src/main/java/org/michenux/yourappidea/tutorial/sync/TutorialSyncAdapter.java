package org.michenux.yourappidea.tutorial.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.michenux.drodrolib.battery.BatteryUtils;
import org.michenux.drodrolib.content.ContentProviderUtils;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.drodrolib.info.AppUsageUtils;
import org.michenux.drodrolib.network.connectivity.ConnectivityUtils;
import org.michenux.drodrolib.network.volley.GsonRequest;
import org.michenux.drodrolib.wordpress.json.WPJsonPost;
import org.michenux.drodrolib.wordpress.json.WPJsonResponse;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.sync.TutorialContentProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class TutorialSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String SYNC_FINISHED = "sync_finished";
    public static final String SYNC_STARTED = "sync_started";

    private ContentResolver mContentResolver ;

    @Inject
    TutorialSyncHelper mSyncHelper;

    /**
     * Set up the sync adapter
     */
    public TutorialSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        ((YourApplication) context.getApplicationContext()).inject(this);
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter()");
        }
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    @TargetApi(11)
    public TutorialSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter()");
        }
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {

        ((YourApplication) getContext().getApplicationContext()).setSyncAdapterRunning(true);
        LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast( new Intent(SYNC_STARTED));

        boolean manualSync = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);

        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync() - manual:" + manualSync );
        }

        WPJsonPost newPost = null;
        try {
            newPost = retrievePosts(AppUsageUtils.getLastSync(this.getContext()), provider);
            if ( newPost != null ) {

                // notification only if not manual sync
                if ( !manualSync ) {
                    sendNotification(newPost);
                }
            }
            else {
                if (BuildConfig.DEBUG) {
                    Log.d(YourApplication.LOG_TAG, "  no new post");
                }
            }

            if ( !manualSync) {
                mSyncHelper.adjustSyncInterval(this.getContext());
            }
            AppUsageUtils.updateLastSync(this.getContext());
        } catch (ParseException e) {
            Log.e(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
            syncResult.stats.numParseExceptions++;
        } catch (InterruptedException | ExecutionException | RemoteException | OperationApplicationException e) {
            Log.e(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
            syncResult.stats.numIoExceptions++;
        } finally {
            LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast( new Intent(SYNC_FINISHED));
            ((YourApplication) getContext().getApplicationContext()).setSyncAdapterRunning(false);
        }
    }

    private void sendNotification(WPJsonPost newPost) {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newPost.getUrl()));
        PendingIntent contentIntent = PendingIntent.getActivity(this.getContext(), 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) this.getContext().getSystemService(this.getContext().NOTIFICATION_SERVICE);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this.getContext());
        notifBuilder.setContentTitle(getContext().getString(R.string.tutorial_notification_title));
        notifBuilder.setContentText(newPost.getTitle());
        notifBuilder.setSmallIcon(R.drawable.ic_stat_notify_newtuto);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notifBuilder.setLights(0xff00ff00, 300, 1000);
        notifBuilder.setContentIntent(contentIntent);
        //notifBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(newPost.title));
        Notification noti = notifBuilder.build();
        //noti.ledARGB = 0xff00ff00;
        //noti.ledOnMS = 300;
        //noti.ledOffMS = 1000;
        //noti.flags |= Notification.FLAG_AUTO_CANCEL;
        //noti.flags |= Notification.FLAG_SHOW_LIGHTS;
        notificationManager.notify(0, noti);
    }

    private WPJsonPost retrievePosts( long lastSync, ContentProviderClient provider ) throws InterruptedException, ExecutionException, ParseException, RemoteException, OperationApplicationException {
        if ( BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter.retrievePosts()");
        }

        WPJsonPost newPost = null;

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        try {
            RequestFuture<WPJsonResponse> future = RequestFuture.newFuture();
            GsonRequest<WPJsonResponse> jsObjRequest = new GsonRequest<WPJsonResponse>(
                    Request.Method.GET,
                    "http://www.michenux.net/?json=get_category_posts&slug=android&custom_fields=android_desc&categories=android&count=9999",
                    WPJsonResponse.class, null, future, future);
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //&include=id,title,custom_fields,url,date,modified,excerpt,author,thumbnail_images,content
            requestQueue.add(jsObjRequest);
            WPJsonResponse response = future.get(); // blocking
            if ( BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "response status: " + response.getStatus());
            }

            if (response.getStatus().equals(WPJsonResponse.STATUS_OK) && response.getPosts() != null && !response.getPosts().isEmpty()) {

                WPJsonPost lastPost = response.getPosts().get(0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                Date lastPostDate = (Date) sdf.parse(lastPost.getDate());
                Date lastSyncDate = new Date(lastSync);

                if ( BuildConfig.DEBUG) {
                    Log.d(YourApplication.LOG_TAG, "title:" + lastPost.getTitle());
                    Log.d(YourApplication.LOG_TAG, "date: " + lastPost.getDate());
                    Log.d(YourApplication.LOG_TAG, "lastSync: " + sdf.format(lastSync));
                }

                if ( lastPostDate.after(lastSyncDate)) {
                    newPost = lastPost;
                }

                updateDatabase(response.getPosts(), provider);

                if (ConnectivityUtils.isConnectedWifi(this.getContext()) && BatteryUtils.isChargingOrFull(this.getContext())) {
                    //load image
                }

            }  else  if ( BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "result is null or empty");
            }
        } finally {
            requestQueue.cancelAll(this.getContext());
        }
        return newPost;
    }

    private void updateDatabase( List<WPJsonPost> posts, ContentProviderClient provider ) throws RemoteException, OperationApplicationException, ParseException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        for( WPJsonPost post : posts ) {
            String desc = "";
            if ( post.getCustomFields() != null && post.getCustomFields().getDescription() != null && !post.getCustomFields().getDescription().isEmpty() ) {
                desc = post.getCustomFields().getDescription().get(0);
            }
            String thumbnail = "";
            if ( post.getThumbnailImages() != null && post.getThumbnailImages().getFoundationFeaturedImage() != null) {
                thumbnail = post.getThumbnailImages().getFoundationFeaturedImage().getUrl();
            }

            Date postCreationDate = (Date) sdf.parse(post.getDate());
            Date postModifDate = (Date) sdf.parse(post.getDate());

            Cursor cursor = getContext().getContentResolver().query(
                    TutorialContentProvider.CONTENT_URI,   // The content URI of the words table
                    new String[] { TutorialContentProvider.DATEMODIFICATION_COLUMN },                        // The columns to return for each row
                    TutorialContentProvider.POSTID_COLUMN + "= ?",                     // Selection criteria
                    new String[]{ Integer.toString(post.getId())},                     // Selection criteria
                    null);

            boolean insertOrModified = false;
            if ( cursor.moveToFirst()) {
                long modificationDate = CursorUtils.getLong(TutorialContentProvider.DATEMODIFICATION_COLUMN, cursor);
                if ( modificationDate != ( postModifDate.getTime() / 1000 )) {

                    if ( BuildConfig.DEBUG ) {
                        Log.d(YourApplication.LOG_TAG, "updated post: " + post.getId());
                    }
                    // delete the old one if modified
                    ops.add(ContentProviderOperation.newDelete(TutorialContentProvider.CONTENT_URI).withSelection(TutorialContentProvider.POSTID_COLUMN + " = ?",
                            new String[]{Integer.toString(post.getId())}).build());
                    insertOrModified = true;

                }
                else {
                    if ( BuildConfig.DEBUG ) {
                        Log.d(YourApplication.LOG_TAG, "unchanged post: " + post.getId() + ", not inserting.");
                    }
                }
            }
            else {
                if ( BuildConfig.DEBUG ) {
                    Log.d(YourApplication.LOG_TAG, "new post: " + post.getId());
                }
                insertOrModified = true;
            }
            cursor.close();

            if ( insertOrModified ) {

                // insert if new or modified
                ops.add(
                        ContentProviderOperation.newInsert(TutorialContentProvider.CONTENT_URI)
                                .withValue(TutorialContentProvider.POSTID_COLUMN, post.getId())
                                .withValue(TutorialContentProvider.TITLE_COLUMN, post.getTitle())
                                .withValue(TutorialContentProvider.DESCRIPTION_COLUMN, post.getExcerpt())
                                .withValue(TutorialContentProvider.THUMBNAIL_COLMUN, thumbnail)
                                .withValue(TutorialContentProvider.URL_COLUMN, post.getUrl())
                                .withValue(TutorialContentProvider.CONTENT_COLUMN, post.getContent())
                                .withValue(TutorialContentProvider.AUTHOR_COLUMN, post.getAuthor().getName())
                                .withValue(TutorialContentProvider.DATECREATION_COLUMN, postCreationDate.getTime() / 1000)
                                .withValue(TutorialContentProvider.DATEMODIFICATION_COLUMN, postModifDate.getTime() / 1000)
                                .withYieldAllowed(false)
                                .build());
            }
        }

        // Keep last 50 posts
        String deleteSelection = TutorialContentProvider.ID_COLUMN + " not in ( select " +
                TutorialContentProvider.ID_COLUMN + " from " + TutorialContentProvider.TABLE_NAME +
                " order by "+ TutorialContentProvider.DATECREATION_COLUMN + " desc limit 50 )";
        String[] deleteParams = new String[]{};
        int actuToDelete = ContentProviderUtils.count(TutorialContentProvider.CONTENT_URI, deleteSelection, deleteParams, this.getContext().getContentResolver());
        if ( BuildConfig.DEBUG ) {
            Log.d(YourApplication.LOG_TAG, actuToDelete + " old posts to delete");
        }
        if ( actuToDelete > 0 ) {
            ops.add(ContentProviderOperation.newDelete(TutorialContentProvider.CONTENT_URI).withSelection(deleteSelection, deleteParams).build());
        }

        if ( !ops.isEmpty()) {
            if ( BuildConfig.DEBUG ) {
                Log.d(YourApplication.LOG_TAG, "execute batch");
            }
            provider.applyBatch(ops);
        }
        else {
            if ( BuildConfig.DEBUG ) {
                Log.d(YourApplication.LOG_TAG, "ignore batch, empty");
            }
        }
    }
 }
