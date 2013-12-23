package org.michenux.yourappidea.aroundme;

import android.location.Location;

import java.util.List;

public interface PlaceProvider {

    public void onLocationChanged(Location location);

    public void onDestroy();

    public interface PlaceLoaderCallback {
        public void onPlaceLoadFinished( List<Place> places );
    }
}
