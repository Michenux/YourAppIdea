package org.michenux.drodrolib.geoloc;

import android.location.Location;

import java.util.Comparator;

public class LocalizableComparator implements Comparator<Localizable> {

    private Location mFromLocation ;

    public LocalizableComparator(Location fromLocation) {
        this.mFromLocation = fromLocation;
    }

    @Override
    public int compare(Localizable loc1, Localizable loc2) {
        float diff = mFromLocation.distanceTo(loc1.getLocation()) - mFromLocation.distanceTo(loc2.getLocation());
        return diff > 0 ? 1 : diff < 0 ? -1 : 0 ;
    }
}
