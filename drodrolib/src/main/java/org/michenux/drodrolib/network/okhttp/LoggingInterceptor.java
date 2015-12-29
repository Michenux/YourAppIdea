package org.michenux.drodrolib.network.okhttp;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.michenux.drodrolib.BuildConfig;
import org.michenux.drodrolib.MCXApplication;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        long t1 = System.nanoTime();

        String url = request.url().toString();

        if (BuildConfig.DEBUG) {
            Log.d(MCXApplication.LOG_TAG, String.format("Sending request %s on %s%n%s",
                    url, chain.connection(), request.headers()));
        }

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();

        if ( BuildConfig.DEBUG ) {
            Log.d(MCXApplication.LOG_TAG, String.format("Received response for %s in %.1fms%n%s",
                    url, (t2 - t1) / 1e6d, response.headers()));
        }

        return response;
    }
}