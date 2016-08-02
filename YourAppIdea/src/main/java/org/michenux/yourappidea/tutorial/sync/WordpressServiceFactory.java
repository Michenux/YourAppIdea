package org.michenux.yourappidea.tutorial.sync;

import android.content.Context;
import android.location.Location;

import com.google.gson.GsonBuilder;

import org.michenux.drodrolib.network.gson.LocationDeserializer;
import org.michenux.drodrolib.network.gson.TimestampDeserializer;
import org.michenux.drodrolib.network.okhttp.LoggingInterceptor;
import org.michenux.yourappidea.R;

import java.sql.Timestamp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordpressServiceFactory {
    public static WordpressService create(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.tutorial_sync_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(WordpressService.class);
    }
}
