package com.spotapps.data;

import com.spotapps.beans.Spot;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tty on 4/7/2016.
 */
public class SpotDataParser {
    public Spot getSpot(String json, String param){
        // params?
        // TODO TALYAC do something
        try {
            JSONObject obj = new JSONObject(json);
            return (Spot)obj.get(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
