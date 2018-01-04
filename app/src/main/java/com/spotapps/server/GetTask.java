package com.spotapps.server;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An AsyncTask implementation for performing GETs on the Hypothetical REST APIs.
 */
public class GetTask extends AsyncTask<String, String, String> {

    private String restUrl;
    private RestTaskCallback callback;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     *
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     *
     */
    public GetTask(String restUrl, RestTaskCallback callback){
        this.restUrl = restUrl;
        this.callback = callback;
    }

//    @Override
//    protected String doInBackground(String... params) {
//        String result = null;
//
//
//
//        HttpClient httpclient = new DefaultHttpClient();
//
//        // Prepare a request object
//        HttpGet httpget = new HttpGet(this.restUrl);
//
//        // Execute the request
//        HttpResponse response;
//        try {
//            response = httpclient.execute(httpget);
//            // Examine the response status
//            // log(response.getStatusLine().toString());
//
//            // Get hold of the response entity
//            HttpEntity entity = response.getEntity();
//            // If the response does not enclose an entity, there is no need
//            // to worry about connection release
//
//            if (entity != null) {
//
//                // A Simple JSON Response Read
//                InputStream instream = entity.getContent();
//                result= convertStreamToString(instream);
//                // now you have the string representation of the HTML request
//
//                instream.close();
//
//            }
//
//
//        } catch (Exception e) {}
//        return result;
//    }

    // this uses HttpUrlConnection. HttpClient can also be used but google recommend the former because it is lightweight, simple and android tailored.
    protected String doInBackground(String... params) {
    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;

    try {
        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        URL url = new URL(this.restUrl);

        // Create the request to OpenWeatherMap, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // TODO check this
            if (buffer.equals(line.charAt(0))) {
                buffer.append(reader.toString());
                buffer.append(";");
            }
            // TODO is it really necessary
            // Stream was empty.  No point in parsing.
            return null;
        }
        forecastJsonStr = buffer.toString();
    } catch (IOException e) {
        Log.e("PlaceholderFragment", "Error ", e);
        // If the code didn't successfully get the weather data, there's no point in attemping
        // to parse it.
        return null;
    } finally{
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e("PlaceholderFragment", "Error closing stream", e);
            }
        }
    }

    return forecastJsonStr;
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

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}
