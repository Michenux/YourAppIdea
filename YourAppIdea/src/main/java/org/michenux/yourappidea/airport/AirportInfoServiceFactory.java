package org.michenux.yourappidea.airport;

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

public class AirportInfoServiceFactory {

    public static AirportInfoService create( Context context ) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.airport_rest_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(AirportInfoService.class);
    }
}
