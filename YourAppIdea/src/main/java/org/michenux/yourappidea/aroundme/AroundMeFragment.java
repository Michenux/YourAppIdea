package org.michenux.yourappidea.aroundme;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import org.michenux.android.db.utils.CursorUtils;
import org.michenux.android.geoloc.DistanceComparator;
import org.michenux.android.network.volley.BitmapCacheHolder;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class AroundMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private LocationClient mLocationClient;

    private LocationRequest mRequest;

    private Geocoder mGeocoder;

    private PlaceListAdapter mPlaceListAdapter;
    private Location mCurrentLocation ;

    private DistanceComparator mDistanceComparator = new DistanceComparator();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private String cityName;

    @Inject
    BitmapCacheHolder mBitmapCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
        setRetainInstance(true);

        mLocationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
        mRequest = LocationRequest.create()
                .setInterval(15000)
                .setFastestInterval(5000)
                .setSmallestDisplacement(50)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mGeocoder = new Geocoder(this.getActivity(), Locale.getDefault());

        mRequestQueue = Volley.newRequestQueue(this.getActivity());
        mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache.getBitmapCache());

        mPlaceListAdapter = new PlaceListAdapter(getActivity(), R.id.aroundme_placename, new ArrayList<Place>(), mImageLoader);

        if (savedInstanceState != null) {
            this.cityName = savedInstanceState.getString("cityName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aroundme_fragment, container, false);

        ListView listView = (ListView) view.findViewById(R.id.aroundme_listview);
        listView.setAdapter(this.mPlaceListAdapter);

        if (cityName != null) {
            TextView textView = (TextView) view.findViewById(R.id.aroundme_cityname);
            textView.setText(cityName);
        }

        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLocationClient.isConnected()) {
            mLocationClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cityName", this.cityName);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(mRequest, this);
    }

    @Override
    public void onDisconnected() {
        mLocationClient.removeLocationUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "AroundmeFragment.onLocationChanged() - new loc: " + location);
        }

        try {
            this.mCurrentLocation = location;

            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                this.cityName = address.getLocality();
                TextView textView = (TextView) getView().findViewById(R.id.aroundme_cityname);
                textView.setText(cityName);
            }

            this.getLoaderManager().restartLoader(1, null, this);

        } catch( Exception e ) {

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Crouton.makeText(
                this.getActivity(),
                getString(R.string.error_connectionfailed),
                Style.ALERT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "AroundmeFragment.onCreateLoader()");
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
        return new CursorLoader(this.getActivity(), PlaceContentProvider.CONTENT_URI, projection, null, null, sort.toString());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "AroundmeFragment.onLoadFinished()");
        }

        this.mPlaceListAdapter.clear();
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

            mPlaceListAdapter.add(place);
        }

        this.mPlaceListAdapter.sort(this.mDistanceComparator);
        this.mPlaceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        this.mPlaceListAdapter.clear();
    }
}
