package org.michenux.yourappidea.aroundme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class AroundMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, PlaceLocalProvider.PlaceLoaderCallback {

    private static final int SELECTCITY_REQUESTCODE = 1000;

    private LocationClient mLocationClient;

    private LocationRequest mRequest;

    private Geocoder mGeocoder;

    private PlaceListAdapter mPlaceListAdapter;

    private String mCityName;

    private PlaceProvider mPlaceProvider;

    private boolean mLocalProvider = true ;

    private boolean mUseLocationClient = true;

    private Location mCurrentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mLocationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
        mRequest = LocationRequest.create()
                .setInterval(15000)
                .setFastestInterval(5000)
                .setSmallestDisplacement(50)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mGeocoder = new Geocoder(this.getActivity(), Locale.getDefault());

        mPlaceListAdapter = new PlaceListAdapter(getActivity(), R.id.aroundme_placename, new ArrayList<Place>());
        if (savedInstanceState != null) {
            this.mUseLocationClient = savedInstanceState.getBoolean("useLocationClient", true);
            this.mCityName = savedInstanceState.getString("cityName");
            this.mCurrentLocation = savedInstanceState.getParcelable("currentLocation");
            this.mLocalProvider = savedInstanceState.getBoolean("localProvider", true);
        }

        if ( this.mLocalProvider ) {
            mPlaceProvider = new PlaceLocalProvider(this, this);
        }
        else {
            mPlaceProvider = new PlaceRemoteProvider(this, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.aroundme_menu, menu);
        MenuItem localMenuItem = menu.findItem(R.id.aroundme_menu_localprovider);
        MenuItem remoteMenuItem = menu.findItem(R.id.aroundme_menu_remoteprovider);
        if ( this.mLocalProvider ) {
            localMenuItem.setChecked(true);
        } else {
            remoteMenuItem.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.aroundme_menu_myposition:
                if ( !this.mUseLocationClient) {
                    this.mUseLocationClient = true ;
                    ((TextView)this.getView().findViewById(R.id.aroundme_cityname)).setText(R.string.aroundme_nolocation);
                    this.mPlaceListAdapter.clear();
                    this.mPlaceListAdapter.notifyDataSetChanged();
                    this.connectLocationClient();
                }
                return true;

            case R.id.aroundme_menu_selectlocation:
                Intent oIntent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(oIntent, SELECTCITY_REQUESTCODE);
                // no animation
                getActivity().overridePendingTransition(0,0);
                return true;

            case R.id.aroundme_menu_localprovider:
                if ( !mLocalProvider) {
                    item.setChecked(true);
                    this.mLocalProvider = true;
                    this.mPlaceProvider = new PlaceLocalProvider(this,this);
                    this.mPlaceProvider.onLocationChanged(this.mCurrentLocation);
                }
                return true;

            case R.id.aroundme_menu_remoteprovider:
                if ( mLocalProvider) {
                    item.setChecked(true);
                    this.mLocalProvider = false;
                    this.mPlaceProvider = new PlaceRemoteProvider(this,this);
                    this.mPlaceProvider.onLocationChanged(this.mCurrentLocation);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aroundme_fragment, container, false);

        ListView listView = (ListView) view.findViewById(R.id.aroundme_listview);
        listView.setAdapter(this.mPlaceListAdapter);
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));

        if (mCityName != null) {
            TextView textView = (TextView) view.findViewById(R.id.aroundme_cityname);
            textView.setText(mCityName);
        }

        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.connectLocationClient();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.disconnectLocationClient();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("useLocationClient", this.mUseLocationClient);
        outState.putParcelable("currentLocation", this.mCurrentLocation);
        outState.putString("cityName", this.mCityName);
        outState.putBoolean("localProvider", this.mLocalProvider);
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

        this.mCurrentLocation = location ;
        try {
            //Todo: test connectivity (connection needed)
            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                this.updateCityView(address.getLocality(), address.getCountryName());
            }

            this.mPlaceProvider.onLocationChanged( location );
        } catch( Exception e ) {
            Log.e(YourApplication.LOG_TAG, "AroundmeFragment.onLocationChanged()", e );
        }
    }

    private void updateCityView( String name, String country ) {
        this.mCityName = name + " (" + country + ")";
        TextView textView = (TextView) getView().findViewById(R.id.aroundme_cityname);
        textView.setText(this.mCityName);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Crouton.makeText(
                this.getActivity(),
                getString(R.string.error_connectionfailed),
                Style.ALERT).show();
    }

    @Override
    public void onPlaceLoadFinished(List<Place> places) {
        this.mPlaceListAdapter.clear();
        for( Place place : places ) {
            mPlaceListAdapter.add(place);
        }
        this.mPlaceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        mPlaceProvider.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == SELECTCITY_REQUESTCODE && resultCode == CityActivity.RESULT_OK) {

            this.updateCityView(data.getStringExtra(CityListFragment.CITY_NAME), data.getStringExtra(CityListFragment.CITY_COUNTRY));

            this.mCurrentLocation = new Location("manual");
            this.mCurrentLocation.setLatitude(data.getDoubleExtra(CityListFragment.CITY_LATITUDE, 0));
            this.mCurrentLocation.setLongitude(data.getDoubleExtra(CityListFragment.CITY_LONGITUDE, 0));
            this.mUseLocationClient = false ;

            this.mPlaceProvider.onLocationChanged(this.mCurrentLocation);
        }
    }

    private void connectLocationClient() {
        if ( mUseLocationClient ) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
            if (resultCode == ConnectionResult.SUCCESS ){
                if (!mLocationClient.isConnected()) {
                    mLocationClient.connect();
                }
            } else{
                GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(), 1 ).show();
            }
        }
    }

    private void disconnectLocationClient() {
        if ( mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }
    }
}
