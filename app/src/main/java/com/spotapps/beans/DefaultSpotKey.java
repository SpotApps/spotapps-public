package com.spotapps.beans;

/**
 * Created by tty on 9/9/2015.
 */
public class DefaultSpotKey implements SpotKey {
    private SpotLocation currentLocation;

    public DefaultSpotKey(SpotLocation currentLocation) {
        this.currentLocation = currentLocation;
    }


    @Override
    public SpotLocation getCurrentLocation() {
        return currentLocation;
    }
}
