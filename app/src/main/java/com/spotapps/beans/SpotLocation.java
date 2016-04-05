package com.spotapps.beans;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by tty on 4/5/2015.
 */
public class SpotLocation implements Serializable {

    private double latitudeA, longitudeA, latitudeB, longitudeB;

    public SpotLocation(Location locationA, Location locationB){
        this.longitudeA = locationA.getLongitude();
        this.latitudeA = locationA.getLatitude();

        this.longitudeB = locationB.getLongitude();
        this.latitudeB = locationB.getLatitude();

    }
}
