package com.spotapps.data;

/**
 * Created by tty on 1/7/2016.
 */
public class LoadedSpot {
    private long id;
    private String spot_json;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpot_json() {
        return spot_json;
    }

    public void setSpot_json(String spot_json) {
        this.spot_json = spot_json;
    }
}
