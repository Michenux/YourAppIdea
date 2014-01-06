package org.michenux.yourappidea.aroundme;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.drodrolib.geoloc.DistanceComparator;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.YourApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceLocalProvider implements PlaceProvider, LoaderManager.LoaderCallbacks<Cursor> {

    private Location mCurrentLocation;

    private Fragment mFragment ;

    private PlaceLoaderCallback mCallback;

    private DistanceComparator mDistanceComparator = new DistanceComparator();

    public PlaceLocalProvider( Fragment fragment, PlaceLoaderCallback callback ) {
        this.mFragment = fragment ;
        this.mCallback = callback;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location ;
        mFragment.getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "PlaceLocalProvider.onCreateLoader()");
        }

        String[] projection = {PlaceContentProvider.NAME_COLUMN, PlaceContentProvider.COUNTRY_COLUMN,
                PlaceContentProvider.URL_COLUMN, PlaceContentProvider.LONGITUDE_COLUMN, PlaceContentProvider.LATITUDE_COLUMN};

        StringBuilder sort = new StringBuilder("abs(");
        sort.append(PlaceContentProvider.LATITUDE_COLUMN);
        sort.append(" - ");
        sort.append(this.mCurrentLocation.getLatitude());
        sort.append(") + abs( ");
        sort.append(PlaceContentProvider.LONGITUDE_COLUMN);
        sort.append(" - ");
        sort.append(this.mCurrentLocation.getLongitude());
        sort.append(") LIMIT 20 ");

        Log.d(YourApplication.LOG_TAG, "TEST: " + PlaceContentProvider.CONTENT_URI.toString());
        return new CursorLoader(this.mFragment.getActivity(), PlaceContentProvider.CONTENT_URI, projection, null, null, sort.toString());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "PlaceLocalProvider.onLoadFinished()");
        }

        List<Place> places = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Place place = new Place();
            place.setName(CursorUtils.getString(PlaceContentProvider.NAME_COLUMN, cursor));
            place.setCountry(CursorUtils.getString(PlaceContentProvider.COUNTRY_COLUMN, cursor));
            place.setImage(CursorUtils.getString(PlaceContentProvider.URL_COLUMN, cursor));
            Location loc = new Location("database");
            loc.setLatitude(CursorUtils.getDouble(PlaceContentProvider.LATITUDE_COLUMN, cursor));
            loc.setLongitude(CursorUtils.getDouble(PlaceContentProvider.LONGITUDE_COLUMN, cursor));
            place.setLocation(loc);
            place.setDistance(loc.distanceTo(this.mCurrentLocation));
            places.add(place);
        }

        Collections.sort(places, this.mDistanceComparator);
        if ( this.mCallback != null ) {
            this.mCallback.onPlaceLoadFinished(places);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        if ( this.mCallback != null ) {
            this.mCallback.onPlaceLoadFinished(new ArrayList<Place>());
        }
    }

    @Override
    public void onDestroy() {

    }
}
