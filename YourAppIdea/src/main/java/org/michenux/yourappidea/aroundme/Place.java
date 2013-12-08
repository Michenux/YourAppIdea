package org.michenux.yourappidea.aroundme;

import android.location.Location;

import org.michenux.android.geoloc.DistanceHolder;
import org.michenux.android.geoloc.Localizable;

public class Place implements DistanceHolder {

    private long id ;
    private String name;
    private String country;
    private Location location;
    private float distance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
