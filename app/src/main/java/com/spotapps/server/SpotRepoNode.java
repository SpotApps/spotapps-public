package com.spotapps.server;

import com.spotapps.beans.Spot;

/**
 * Created by tty on 5/31/2016.
 */
public class SpotRepoNode {

    public SpotRepoNode(double distance, Spot spot) {
        this.distance = distance;
        this.spot = spot;
    }

    public Spot getSpot() {
        return spot;
    }

    public double getDistance() {
        return distance;
    }

    double distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpotRepoNode)) return false;

        SpotRepoNode that = (SpotRepoNode) o;

        if (Double.compare(that.distance, distance) != 0) return false;
        return spot.equals(that.spot);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(distance);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + spot.hashCode();
        return result;
    }

    Spot spot;
}
