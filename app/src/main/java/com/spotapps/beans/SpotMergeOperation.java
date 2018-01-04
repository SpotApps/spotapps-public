package com.spotapps.beans;

/**
 * Created by tty on 15/12/2016.
 */

public class SpotMergeOperation {
    private final SpotLocation currentLocation;
    private final double updatedLatitude;
    private final double updatedLongitude;

    public SpotMergeOperation(SpotLocation currentLocation, double updatedLatitude, double updatedLongitude) {

        this.currentLocation = currentLocation;
        this.updatedLatitude = updatedLatitude;
        this.updatedLongitude = updatedLongitude;
    }

    public SpotLocation execute() {
        double oldMinLatitude = currentLocation.getMinLat();
        double oldMinLongitude = currentLocation.getMinLong();
        double oldMaxLatitude = currentLocation.getMaxLat();
        double oldMaxLongitude = currentLocation.getMaxLong();
        double minLatitude = oldMinLatitude;
        double minLongitude = oldMinLongitude;
        double maxLatitude = oldMaxLatitude;
        double maxLongitude = oldMaxLongitude;
        if (updatedLatitude < minLatitude)
        {
            //if (Math.abs(minLatitude - updatedLatitude) < spot.getTypeSize()){
            minLatitude = updatedLatitude;
//            } else {
//                minLatitude = oldMinLatitude;
//            }
        }
        if (updatedLatitude > maxLatitude){
            maxLatitude = updatedLatitude;
        }
        if (updatedLongitude < minLongitude){
            minLongitude = updatedLongitude;
        }
        if (updatedLongitude > maxLongitude){
            maxLongitude = updatedLongitude;
        }

        SpotLocation location = new SpotLocation(minLatitude, minLongitude, maxLatitude, maxLongitude);
        return location;
    }
}
