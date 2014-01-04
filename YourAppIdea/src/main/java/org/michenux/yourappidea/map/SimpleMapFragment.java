package org.michenux.yourappidea.map;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.michenux.drodrolib.ui.map.MCXSupportMapFragment;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class SimpleMapFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, MCXSupportMapFragment.OnMapCreatedListener {

    private GoogleMap mMap;

    private LocationClient mLocationClient;

    private LocationRequest mRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "simpleMapFragment.onCreate");
        }

        mLocationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
        mRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(16)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "simpleMapFragment.onCreateView");
        }
        View view = container.findViewById(R.id.simplemap);
        view = inflater.inflate(R.layout.simplemap_fragment, container, false);
        return view ;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MCXSupportMapFragment mapFragment = getMapFragment();
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "simpleMapFragment.onViewCreated - mapFragment = " + mapFragment);
        }
        if ( mapFragment == null ) {
            mapFragment = new MCXSupportMapFragment();
            mapFragment.setOnMapCreatedListener(this);
            mapFragment.setRetainInstance(true);
            getChildFragmentManager().beginTransaction().replace(R.id.simplemap, mapFragment, null).commit();
        }
        else {
            this.mMap = mapFragment.getMap();
        }
    }

    private MCXSupportMapFragment getMapFragment() {
        return (MCXSupportMapFragment) getChildFragmentManager().findFragmentById(R.id.simplemap);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationClient.connect();
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
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(mRequest, this);
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "simpleMapFragment.onLocationChanged");
        }
        if ( mMap != null ) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 15);
            mMap.animateCamera(cameraUpdate);
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
    public void onMapCreated(GoogleMap googleMap) {
        if (BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "simpleMapFragment.onMapCreated - map: " + googleMap);
        }
        if ( googleMap != null ) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
        }
     }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.simplemap_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.simplemap_menu_info:
                SimpleDialogFragment.createBuilder(this.getActivity(), this.getActivity().getSupportFragmentManager())
                        .setMessage(Html.fromHtml(getString(R.string.simplemap_info_details)))
                        .setTitle(R.string.simplemap_info_title)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
