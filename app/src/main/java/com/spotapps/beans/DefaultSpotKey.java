package com.spotapps.beans;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by tty on 9/9/2015.
 */
public class DefaultSpotKey implements SpotKey, Serializable {
    private final double currentLatitude;
    private final double currentLongitude;

    public DefaultSpotKey(Location currentLocation) {

        currentLongitude = currentLocation.getLongitude();
        currentLatitude = currentLocation.getLatitude();
    }

    @Override
    public double getCurrentLatitude() {
        return currentLatitude;
    }

    @Override
    public double getCurrentLongitude() {
        return currentLongitude;
    }
}
