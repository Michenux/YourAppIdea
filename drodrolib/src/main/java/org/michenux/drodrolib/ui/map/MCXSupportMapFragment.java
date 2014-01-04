package org.michenux.drodrolib.ui.map;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MCXSupportMapFragment extends SupportMapFragment {

    private OnMapCreatedListener onMapCreatedListener ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ( onMapCreatedListener != null ) {
            onMapCreatedListener.onMapCreated(getMap());
        }
    }

    public OnMapCreatedListener getOnMapCreatedListener() {
        return onMapCreatedListener;
    }

    public void setOnMapCreatedListener(OnMapCreatedListener onMapCreatedListener) {
        this.onMapCreatedListener = onMapCreatedListener;
    }

    public static interface OnMapCreatedListener {

        public void onMapCreated( GoogleMap googleMap);
    }
}
