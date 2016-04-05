package com.spotapps.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * android lazy loading images and text in listview from http json data
 * Created by tty on 4/20/2015.
 */
public class SpotParser {
// TODO talyac change spots
    public static final String SPOTS_JSON = "spots";

    public List<HashMap<String, Object>> parse(JSONObject jObject){
        JSONArray spots = null;
        try {
            spots = jObject.getJSONArray(SPOTS_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // TODO talyac hmmm... no
    }

}
