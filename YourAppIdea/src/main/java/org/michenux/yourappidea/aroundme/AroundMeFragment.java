package org.michenux.yourappidea.aroundme;

import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

    private LocationClient mLocationClient;

    private LocationRequest mRequest;

    private Geocoder mGeocoder;

    private PlaceListAdapter mPlaceListAdapter;

    private String mCityName;

    private PlaceProvider mPlaceProvider ;

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

        mPlaceListAdapter = new PlaceListAdapter(getActivity(), R.id.aroundme_placename, new ArrayList<Place>());
        if (savedInstanceState != null) {
            this.mCityName = savedInstanceState.getString("cityName");
        }

        //mPlaceProvider = new PlaceLocalProvider(this, this);
        mPlaceProvider = new PlaceRemoteProvider(this, this);
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

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS){
            if (!mLocationClient.isConnected()) {
                mLocationClient.connect();
            }
        } else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(), 1 ).show();
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
        outState.putString("cityName", this.mCityName);
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
            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                this.mCityName = address.getLocality() + " (" + address.getCountryName() + ")";
                TextView textView = (TextView) getView().findViewById(R.id.aroundme_cityname);
                textView.setText(this.mCityName);
            }

            this.mPlaceProvider.onLocationChanged( location );
        } catch( Exception e ) {
            Log.e(YourApplication.LOG_TAG, "AroundmeFragment.onLocationChanged()", e );
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
}
