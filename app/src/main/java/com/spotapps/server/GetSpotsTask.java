package com.spotapps.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotBundle;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tty on 4/7/2016.
 * TODO TALYAC this should use GetTask and the url should use UriBuilder (https://www.udacity.com/course/viewer#!/c-ud853/l-1469948762/e-1630778633/m-1630778636)
 */
public class GetSpotsTask extends AsyncTask<SpotKey, Integer, SpotBundle> {


    private static final double RANGE_FOR_SEARCH = 100;//0.5d;

    public GetSpotsTask(Context context) {
        this.context = context;
    }

    private Context context;

    protected SpotBundle doInBackground(SpotKey... keys) {
        /*
            To ensure that a task is cancelled as quickly as possible,
            you should always check the return value of isCancelled() periodically from doInBackground(Object[]),
            if possible (inside a loop for instance.)
            if possible (inside a loop for instance.)
         */
        // TODO extract location data from key
        SpotKey key = keys[0];
        if (key == null){
            throw new RuntimeException("AHHHHHHHH!!!!!");
        }
        AWSAdapter awsAdapter = new AWSAdapter();
        List<Spot> result = awsAdapter.loadSpots(this.context);
        // publishProgress(50);
        // TODO TALYAC here we can call publishProgress() while filtering the spots
        List<Spot> filteredResult = filterAndSortResultByLocation(result, key.getCurrentLatitude(), key.getCurrentLongitude());
        // do something with filteredResult or simply return mock data ;)

        // load the mini apps for the first spot
        SpotBundle spotBundle = null;
        if (filteredResult.size() > 0) {
            Spot spot = filteredResult.get(0);
            List<MiniApp> listMiniApp = awsAdapter.loadMiniApps(this.context, spot);

            spotBundle = new SpotBundle(key, filteredResult, SpotFactory.createNewFullSpot(spot, listMiniApp), SpotBundle.BundleStatus.LOADED);
        }

        // TODO TALYAC mock start
        String paramString = "Put something meaningful here";
        if (result != null){

            Log.d("GetSpotsTask", paramString );
        }
//        Spot spot = ServerFacade.loadSpot(paramString);
//        List<Spot> altResult = new ArrayList<>();
//
//        altResult.add(spot);
        // TODO TALYAC mock end

        return spotBundle;


//        DynamoDB dynamoDB = new DynamoDB(
//                new AmazonDynamoDBClient(new ProfileCredentialsProvider()));
//
//        Table table = dynamoDB.getTable("Reply");
//
//        QuerySpec spec = new QuerySpec()
//                .withKeyConditionExpression("Id = :v_id")
//                .withValueMap(new ValueMap()
//                        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));
//
//        ItemCollection<QueryOutcome> items = table.query(spec);
//
//        Iterator<ClipData.Item> iterator = items.iterator();
//        Item item = null;
//        while (iterator.hasNext()) {
//            item = iterator.next();
//            System.out.println(item.toJSONPretty());
//        }
//
//        // These two need to be declared outside the try/catch
//        // so that they can be closed in the finally block.
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain the raw JSON response as a string.
//        String result = null;
//// http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280
//        try {
////            Uri.Builder builder = new Uri.Builder();
////            builder.appendPath("localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
////            Uri uri = builder.build();
////            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
//            URL url = new URL("http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
//
//            // http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280
//            // Create the request  and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            result = buffer.toString();
//        } catch (IOException e) {
//            Log.e("PlaceholderFragment", "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attemping
//            // to parse it.
//            return null;
//        } finally{
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("PlaceholderFragment", "Error closing stream", e);
//                }
//            }
//            //////////////////////////
//            // do something with resultStr or simply return mock data ;)
//            if (result != null){
//                Log.d("GetSpotsTask", result);
//            }
//            Spot spot = ServerFacade.loadSpot(result);
//            List<Spot> altResult = new ArrayList<>();
//            // here we can call publishProgress() while loading the spots
//            // TODO TALYAC mock
//            altResult.add(spot);
//            return altResult;
//        }
    }

    private List<Spot> filterAndSortResultByLocation(List<Spot> result, double currentLatitude, double currentLongitude) {
        List<Spot> nearbySpots = new ArrayList<Spot>();
        for (Spot spot:result) {

            if (spot.getLocation().isInRange(currentLatitude, currentLongitude,RANGE_FOR_SEARCH))
            {
                nearbySpots.add(spot);
            }
        }
        Collections.sort(nearbySpots, new SpotDistanceComparator(currentLatitude, currentLongitude));
        // TODO TALYAC - if the size of the list is too large filter recursively
        // TODO TALYAC - list should also be sorted
        return nearbySpots;
    }

//    protected List<Spot> doInBackgroundForOpenShift(SpotKey... keys) {
//        DynamoDB dynamoDB = new DynamoDB(
//                new AmazonDynamoDBClient(new ProfileCredentialsProvider()));
//
//        Table table = dynamoDB.getTable("Reply");
//
//        QuerySpec spec = new QuerySpec()
//                .withKeyConditionExpression("Id = :v_id")
//                .withValueMap(new ValueMap()
//                        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));
//
//        ItemCollection<QueryOutcome> items = table.query(spec);
//
//        Iterator<ClipData.Item> iterator = items.iterator();
//        Item item = null;
//        while (iterator.hasNext()) {
//            item = iterator.next();
//            System.out.println(item.toJSONPretty());
//        }
//
//        // These two need to be declared outside the try/catch
//        // so that they can be closed in the finally block.
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain the raw JSON response as a string.
//        String result = null;
//// http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280
//        try {
////            Uri.Builder builder = new Uri.Builder();
////            builder.appendPath("localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
////            Uri uri = builder.build();
////            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
//            URL url = new URL("http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
//
//            // http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280
//            // Create the request  and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            result = buffer.toString();
//        } catch (IOException e) {
//            Log.e("PlaceholderFragment", "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attemping
//            // to parse it.
//            return null;
//        } finally{
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("PlaceholderFragment", "Error closing stream", e);
//                }
//            }
//            //////////////////////////
//            // do something with resultStr or simply return mock data ;)
//            if (result != null){
//                Log.d("GetSpotsTask", result);
//            }
////            Spot spot = ServerFacade.loadSpot(result);
////            List<Spot> altResult = new ArrayList<>();
//            // here we can call publishProgress() while loading the spots
//            // TODO TALYAC mock
//            altResult.add(spot);
//            return altResult;
//        }
//    }

    // This is not being used. See doInBackground
//    protected List<Spot> doInBackgroundForAmazonUrl(SpotKey... keys) {
//        // These two need to be declared outside the try/catch
//        // so that they can be closed in the finally block.
//            HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain the raw JSON response as a string.
//        String result = null;
//// http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280
//        try {
////            Uri.Builder builder = new Uri.Builder();
////            builder.appendPath("localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
////            Uri uri = builder.build();
////            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
////            URL url = new URL("http://localcloud-lcserver.rhcloud.com/rest/xml/31.9960-34.8280");
//            URL url = new URL("http://dynamodb.us-west-2.amazonaws.com");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//           // Authorization: AWS4-HMAC-SHA256 Credential=AccessKeyID/20130112/us-west-2/dynamodb/aws4_request,SignedHeaders=host;x-amz-date;x-amz-target,Signature=145b1567ab3c50d929412f28f52c45dbf1e63ec5c66023d232a539a4afd11fd9
//            urlConnection.addRequestProperty("x-amz-date", "20160102T092034Z");
//            urlConnection.addRequestProperty("x-amz-target", "DynamoDB_20120810.GetItem");
//            urlConnection.addRequestProperty("content-type", "application/x-amz-json-1.0");
//
////            content-length: 23
////            connection: Keep-Alive
////            {
////                "TableName": "my-table",
////                    "Key": {
////                "Name": {"S": "Back To The Future"},
////                "Year": {"S": "1985"}
////            }
//
//
//
//            // Create the request  and open the connection
//
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            result = buffer.toString();
//        } catch (IOException e) {
//            Log.e("PlaceholderFragment", "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attemping
//            // to parse it.
//            return null;
//        } finally{
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("PlaceholderFragment", "Error closing stream", e);
//                }
//            }
//            //////////////////////////
//            // do something with resultStr or simply return mock data ;)
//            if (result != null){
//                Log.d("GetSpotsTask", result);
//            }
//            Spot spot = ServerFacade.loadSpot(result);
//            List<Spot> altResult = new ArrayList<>();
//            // here we can call publishProgress() while loading the spots
//            // TODO TALYAC mock
//            altResult.add(spot);
//            return altResult;
//        }
//    }

    // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
    protected void onProgressUpdate(Integer... progress) {
//        if (progress != null && progress.length > 0){
//            setLoadingCounter(progress[0]);
//        }
    }

    protected void onPostExecute(List<Spot> result) {
        // TODO TALYAC update the adapter with the new data
//            if (result!= null && result.size() > 0){
//                setLoadedSpot(result.get(0));
//            }
    }

//    private void simulate5SecondsWaitTime() {
//        int count = 5;
//        ///// load spots
//        for (int i = 0; i < count; i++) {
//            try {
//                synchronized (this){
//                    wait(1000);
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            publishProgress(i);
//            // TODO TALYAC result.add;
//            publishProgress((int) ((i / (float) count) * 100));
//            // Escape early if cancel() is called
//            if (isCancelled()) break;
//        }
//    }
}
