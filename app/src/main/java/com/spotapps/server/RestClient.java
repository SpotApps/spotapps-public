package com.spotapps.server;

import android.provider.ContactsContract;

import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotLocation;
import com.spotapps.sensors.SensorsFacade;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tty on 4/8/2015.
 */
public class RestClient {

    public Spot loadCurrentSpot() {

        String restUrl = constructRestUrlForProfile(null);
        String spotName = "temp"; //////////////////////////////////uglyREST(restUrl, null);
//        new GetTask(restUrl, new RestTaskCallback() {
//            @Override
//            public void onTaskComplete(String response) {
//                parseResponseAndUpdateUIPerhapsUsingCallback();
//            }
//
//            private void parseResponseAndUpdateUIPerhapsUsingCallback() {
//                // TODO TALYAC implement
//            }
//        }).execute();
        /////////////////
        SpotLocation spotLocation = SensorsFacade.getCurrentLocation();
        return SpotFactory.createSpot(spotName, spotLocation);
        /////////////////
    }

    private String uglyREST(String url, String... params) {

            String result = null;
            HttpClient httpclient = new DefaultHttpClient();

            // Prepare a request object
            HttpGet httpget = new HttpGet(url);

            // Execute the request
            HttpResponse response;
            try {
                response = httpclient.execute(httpget);
                // Examine the response status
                // log(response.getStatusLine().toString());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();
                // If the response does not enclose an entity, there is no need
                // to worry about connection release

                if (entity != null) {

                    // A Simple JSON Response Read
                    InputStream instream = entity.getContent();
                    result= convertStreamToString(instream);
                    // now you have the string representation of the HTML request

                    instream.close();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    private String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String constructRestUrlForProfile(SpotLocation spotLocation) {
        return "http://192.168.1.102:3500/geo?lon=34.8&lat=31.96";
    }
}