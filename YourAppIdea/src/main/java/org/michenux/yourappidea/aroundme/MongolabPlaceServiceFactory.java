package org.michenux.yourappidea.aroundme;

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

public class MongolabPlaceServiceFactory {

    public static MongolabPlaceService create( Context context ) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        String url = context.getString(R.string.aroundme_placeremoteprovider_url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(MongolabPlaceService.class);
    }
}
