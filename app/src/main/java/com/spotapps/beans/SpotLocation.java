package com.spotapps.beans;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by tty on 4/5/2015.
 */
public class SpotLocation implements Serializable {

    public double getMinLat() {
        return minLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMaxLong() {
        return maxLong;
    }

    private double minLat, minLong, maxLat, maxLong;

    public SpotLocation(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude){
        this.minLong = minLongitude;
        this.minLat = minLatitude;
        this.maxLong = maxLongitude;
        this.maxLat = maxLatitude;
    }

    // if
    public boolean isInRange(double spotLat, double spotLong, double range) {
        return distanceFromSpot(spotLat, spotLong) < range;
    }

    public double distanceFromSpot(double spotLat, double spotLong){
        double latDistance = 0;
        if (spotLat < this.minLat) latDistance = this.minLat - spotLat;
        if (spotLat > this.maxLat) latDistance = spotLat - this.maxLat;
        double longDistance = 0;
        if (spotLong < this.minLong) longDistance = this.minLong - spotLong;
        if (spotLong > this.maxLong) longDistance = spotLong - this.maxLong;

        double distance = Math.sqrt(latDistance * latDistance + longDistance * longDistance);
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpotLocation)) return false;

        SpotLocation that = (SpotLocation) o;

        if (Double.compare(that.minLat, minLat) != 0) return false;
        if (Double.compare(that.minLong, minLong) != 0) return false;
        if (Double.compare(that.maxLat, maxLat) != 0) return false;
        return Double.compare(that.maxLong, maxLong) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(minLat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
