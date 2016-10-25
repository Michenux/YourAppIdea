package org.michenux.yourappidea.aroundme;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.michenux.drodrolib.ui.snackbar.SnackbarHelper;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlaceRemoteProvider implements PlaceProvider {
    private Location mCurrentLocation;

    private PlaceLoaderCallback mCallback;

    private boolean mRequestRunning = false;

    private Fragment mFragment;

    private MongolabPlaceService mPlaceService;

    public PlaceRemoteProvider(Fragment fragment, PlaceLoaderCallback callback) {
        this.mCallback = callback;
        this.mFragment = fragment;
        mPlaceService = MongolabPlaceServiceFactory.create(fragment.getContext());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        this.startRequest();
    }

    private void startRequest() {
        if (!mRequestRunning) {
            this.mRequestRunning = true;

            String query = "{\\\'location\\\':{$near:{$geometry:{\\\'type\':\\\'Point\\\',\\\'coordinates\':["
                    + this.mCurrentLocation.getLongitude() + "," + this.mCurrentLocation.getLatitude()
                    + "]},$maxDistance:1000000000}}}";

            Observable<List<Place>> observable =
                    mPlaceService.getPlaces(query, mFragment.getString(R.string.aroundme_apiKey));

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<List<Place>>() {
                        @Override
                        public void onCompleted() {
                            PlaceRemoteProvider.this.mRequestRunning = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(YourApplication.LOG_TAG, "PlaceRemoteProvider.onErrorResponse", e.getCause());
                            PlaceRemoteProvider.this.mRequestRunning = false;

                            SnackbarHelper.showInfoLongMessage(PlaceRemoteProvider.this.mFragment.getView(), R.string.error_retrievingdata);
                        }

                        @Override
                        public void onNext(List<Place> places) {
                            if (BuildConfig.DEBUG) {
                                Log.i(YourApplication.LOG_TAG, "PlaceRemoteProvider.onResponse");
                            }

                            for (Place place : places) {
                                place.setDistance(PlaceRemoteProvider.this.mCurrentLocation.distanceTo(place.getLocation()));
                            }

                            if (PlaceRemoteProvider.this.mCallback != null) {
                                PlaceRemoteProvider.this.mCallback.onPlaceLoadFinished(places);
                            }
                        }
                    });

        } else if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "  request is already running");
        }
    }

    private void cancelRequests() {
    }

    @Override
    public void onDestroy() {
        this.cancelRequests();
    }
}
