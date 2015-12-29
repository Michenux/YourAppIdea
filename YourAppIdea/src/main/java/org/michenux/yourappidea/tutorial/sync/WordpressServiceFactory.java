package org.michenux.yourappidea.tutorial.sync;

import android.content.Context;
import android.location.Location;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.michenux.drodrolib.network.gson.LocationDeserializer;
import org.michenux.drodrolib.network.gson.TimestampDeserializer;
import org.michenux.drodrolib.network.okhttp.LoggingInterceptor;
import org.michenux.yourappidea.R;

import java.sql.Timestamp;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class WordpressServiceFactory {

    public static WordpressService create( Context context ) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.tutorial_sync_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(WordpressService.class);
    }
}
