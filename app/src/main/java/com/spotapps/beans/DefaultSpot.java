package com.spotapps.beans;

/**
 * Created by tty on 4/6/2015.
 */
public class DefaultSpot implements Spot {
    private SpotLocation spotLocation;
    private String name;

    public DefaultSpot(String name, SpotLocation location) {
        this.name = name;
        this.spotLocation = location;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public SpotLocation getLocation() {
        return spotLocation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        return sb.toString();
    }
}
