package com.spotapps.beans;

/**
 * Created by tty on 4/6/2015.
 */
public class DefaultSpot implements Spot {
    private SpotLocation spotLocation;
    private String name;
    private String type;

    @Override
    public String getId() {
        return id;
    }

    public SpotLocation getLocation() {
        return spotLocation;
    }

    private String id;

    public DefaultSpot(String id, String name, SpotLocation location, String type) {
        this.id = id;
        this.name = name;
        this.spotLocation = location;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getTypeSize() {
        return 10;
        // TODO TALYAC do this according to the type of spot
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        return sb.toString();
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
