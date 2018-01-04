package com.spotapps.server;

import com.spotapps.beans.Spot;

import java.util.Comparator;

/**
 * Created by tty on 5/31/2016.
 */
public class SpotDistanceComparator implements Comparator<Spot> {
    private static final double ACCURACY = 1000000;
    private final double spotLat;
    private final double spotLong;

    public SpotDistanceComparator(double spotLat, double spotLong) {
        this.spotLat = spotLat;
        this.spotLong = spotLong;
    }

    @Override
    public int compare(Spot lhs, Spot rhs) {

        Double lhsDistance = lhs.getLocation().distanceFromSpot(spotLat, spotLong) * ACCURACY;
        Double rhsDistance = rhs.getLocation().distanceFromSpot(spotLat, spotLong) * ACCURACY;
        return lhsDistance.intValue() - rhsDistance.intValue();
    }
}
