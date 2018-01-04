package com.spotapps.sensors;

import android.location.Location;

import com.spotapps.beans.SpotLocation;

/**
 * Created by tty on 4/7/2015.
 */
public class SensorsFacade {

    public static Location getCurrentLocation(){
        String str_lat_start = "29.287260";
        String str_lon_start = "80.100327";
        String str_lat_end = "26.016045";
        String str_lon_end = "81.150169";

        double lat_start = 0;
        double lon_start = 0;
        double lat_end = 0;
        double lon_end = 0;

        try {

            lat_start = Double.parseDouble( str_lat_start );
            lon_start = Double.parseDouble( str_lon_start );
            lat_end = Double.parseDouble( str_lat_end );
            lon_end = Double.parseDouble( str_lon_end );

        } catch (NumberFormatException e) {
            // TODO TALYAC take care of the exception
            throw e;
        }


        return null;
    }


    public static SpotLocation getNewLocation(){
        String str_lat_start = "28.287260";
        String str_lon_start = "81.100327";
        String str_lat_end = "27.016045";
        String str_lon_end = "82.150169";

        double lat_start = 0;
        double lon_start = 0;
        double lat_end = 0;
        double lon_end = 0;

        try {

            lat_start = Double.parseDouble( str_lat_start );
            lon_start = Double.parseDouble( str_lon_start );
            lat_end = Double.parseDouble( str_lat_end );
            lon_end = Double.parseDouble( str_lon_end );

        } catch (NumberFormatException e) {
            // TODO TALYAC take care of the exception
            throw e;
        }

        SpotLocation spotLocation = new SpotLocation(lat_start, lon_start, lat_end, lon_end);
        return spotLocation;
    }
}