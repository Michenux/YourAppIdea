package org.michenux.yourappidea.tutorial.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.michenux.android.network.volley.GsonRequest;
import org.michenux.android.wordpress.json.WPJsonPost;
import org.michenux.android.wordpress.json.WPJsonResponse;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class TutorialSyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver mContentResolver ;

    @Inject
    TutorialSyncHelper mSyncTutorialSyncHelper;

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

        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()");
        }

        WPJsonPost newPost = null;
        try {
            newPost = getNewJsonPost(this.mSyncTutorialSyncHelper.getLastTutoSync(this.getContext()));
            if ( newPost != null ) {

                this.mSyncTutorialSyncHelper.updateLastTutoSync(this.getContext());

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
            else {
                if (BuildConfig.DEBUG) {
                    Log.d(YourApplication.LOG_TAG, "  no new post");
                }
            }
        } catch (ParseException e) {
            Log.e(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
            syncResult.stats.numParseExceptions++;
        } catch (InterruptedException e) {
            Log.e(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
            syncResult.stats.numIoExceptions++;
        } catch (ExecutionException e) {
            Log.e(YourApplication.LOG_TAG, "tutorialSyncAdapter.onPerformSync()", e);
            syncResult.stats.numIoExceptions++;
        }
    }


    private WPJsonPost getNewJsonPost( long lastSync ) throws InterruptedException, ExecutionException, ParseException {

        if ( BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "tutorialSyncAdapter.getNewJsonPost()");
        }
        WPJsonPost newPost = null;

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        try {
            RequestFuture<WPJsonResponse> future = RequestFuture.newFuture();
            GsonRequest<WPJsonResponse> jsObjRequest = new GsonRequest<WPJsonResponse>(
                    Request.Method.GET,
                    getContext().getString(R.string.tutorial_sync_url),
                    WPJsonResponse.class, null, future, future);

            requestQueue.add(jsObjRequest);
            WPJsonResponse response = future.get(); // blocking
            if ( BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "response status: " + response.getStatus());
            }
            if (response.getPosts() != null && !response.getPosts().isEmpty()) {

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
            } else  if ( BuildConfig.DEBUG) {
                Log.d(YourApplication.LOG_TAG, "result is null or empty");
            }
        } finally {
            requestQueue.cancelAll(this.getContext());
        }
        return newPost;
    }
}
