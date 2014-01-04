package org.michenux.yourappidea.aroundme;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.michenux.drodrolib.network.volley.GsonRequest;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PlaceRemoteProvider implements PlaceProvider {

    private Location mCurrentLocation;

    private PlaceLoaderCallback mCallback;

    private RequestQueue mRequestQueue;

    private boolean mRequestRunning = false;

    private Fragment mFragment ;

    public PlaceRemoteProvider( Fragment fragment, PlaceLoaderCallback callback ) {
        this.mCallback = callback;
        this.mFragment = fragment;
        this.mRequestQueue = Volley.newRequestQueue(fragment.getActivity());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location ;
        this.startRequest();
    }

    private void startRequest() {
        if ( !mRequestRunning ) {
            this.mRequestRunning = true ;

            String url = mFragment.getString(R.string.aroundme_placeremoteprovider_url, this.mCurrentLocation.getLongitude(), this.mCurrentLocation.getLatitude(), 1000000000);
            GsonRequest<PlaceRestResponse> jsObjRequest = new GsonRequest<PlaceRestResponse>(
                    Request.Method.GET, url,
                    PlaceRestResponse.class, null,
                    this.createPlaceRequestSuccessListener(),
                    this.createPlaceRequestErrorListener());
            jsObjRequest.setShouldCache(false);
            this.mRequestQueue.add(jsObjRequest);
        }
        else if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "  request is already running");
        }
    }

    private void cancelRequests() {
        this.mRequestQueue.cancelAll(this);
    }

    private Response.Listener<PlaceRestResponse> createPlaceRequestSuccessListener() {
        return new Response.Listener<PlaceRestResponse>() {
            @Override
            public void onResponse(PlaceRestResponse response) {
                if (BuildConfig.DEBUG) {
                    Log.i(YourApplication.LOG_TAG, "PlaceRemoteProvider.onResponse");
                }
                PlaceRemoteProvider.this.mRequestRunning = false ;

                for( Place place : response) {
                    place.setDistance(PlaceRemoteProvider.this.mCurrentLocation.distanceTo(place.getLocation()));
                }

                if ( PlaceRemoteProvider.this.mCallback != null ) {
                    PlaceRemoteProvider.this.mCallback.onPlaceLoadFinished(response);
                }
            }
        };
    }

    private Response.ErrorListener createPlaceRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (BuildConfig.DEBUG) {
                    Log.e(YourApplication.LOG_TAG, "PlaceRemoteProvider.onErrorResponse", error.getCause());
                }
                PlaceRemoteProvider.this.mRequestRunning = false ;

                Crouton.makeText(
                        PlaceRemoteProvider.this.mFragment.getActivity(),
                        PlaceRemoteProvider.this.mFragment.getString(R.string.error_retrievingdata),
                        Style.ALERT).show();
            }
        };
    }

    @Override
    public void onDestroy() {
        this.cancelRequests();
        Crouton.cancelAllCroutons();
    }
}
