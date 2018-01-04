package com.spotapps.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tty on 7/21/2016.
 */
public class SpotBundle implements Serializable{

    private final List<Spot> spots;
    private FullSpot firstSpot;
    private final SpotKey key;
    private transient BundleStatus status;



    public SpotBundle(SpotKey key, List<Spot> spots, FullSpot firstSpot, BundleStatus status) {
        this.key = key;
        this.spots = spots;
        this.firstSpot = firstSpot;
        this.status = status;
    }

    public SpotKey getKey() {
        return key;
    }

    public List<Spot> getSpots() {
      return spots;
    }

    public FullSpot getFirstSpot() {
        return firstSpot;
    }

    public void updateFirstSpot(FullSpot firstSpot) {
        this.firstSpot = firstSpot;

    }

    public enum BundleStatus {
        VIEW_NEARBY_SPOTS, LOADED, BACK
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<SpotBundle>");
        if (getFirstSpot() != null){
            sb.append("<FirstSpot>");
            sb.append(getFirstSpot().toString());
            sb.append("</FirstSpot>");
        }
        if (getSpots() != null){
            sb.append("<SpotsListSize>");
            sb.append(getSpots().size());
            sb.append("</SpotsListSize>");
        }
        sb.append("</SpotBundle>");
        return sb.toString();
    }
}
