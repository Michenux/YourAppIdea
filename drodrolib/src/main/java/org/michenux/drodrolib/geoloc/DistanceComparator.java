package org.michenux.drodrolib.geoloc;

import java.util.Comparator;

public class DistanceComparator implements Comparator<DistanceHolder> {

    @Override
    public int compare(DistanceHolder dh1, DistanceHolder dh2) {
        float diff = dh1.getDistance() - dh2.getDistance();
        return diff > 0 ? 1 : diff < 0 ? -1 : 0 ;
    }
}
