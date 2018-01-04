package com.spotapps.server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppFactory;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import com.spotapps.beans.SpotLocation;
import com.spotapps.server.miniappgenerator.MiniAppGenerator;
import com.spotapps.server.miniappgenerator.MiniAppGeneratorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tty on 4/24/2016.
 */
public class AWSAdapter {

    public static final String LOG_TAG = "SpotAppsAWSAdapter";
    public static final String SPOT_TABLE_NAME = "Spot";
    public static final String MINI_APP_TABLE_NAME = "MiniApp";
    private SpotRepository spotRepository = new SpotRepository();


    @NonNull
    private AmazonDynamoDB createAmazonDynamoDB(Context context) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,    /* get the context for the application */
                // "BLABLABLA",  /* account id */
                "us-east-1:77765ab4-dc7e-44a8-a46c-0047f027e9b5",    /* Identity Pool ID */
                //  "", //BLA BLA authenticated role ARN
                //  "", //BLA BLA unauthenticated role ARN
                Regions.US_EAST_1           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );
        return new AmazonDynamoDBClient(credentialsProvider);
    }

    public List<Spot> loadSpots(Context context) {
        try {
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);
            List<String> listOfAtts = new ArrayList<>();
            listOfAtts.add("Id");
            listOfAtts.add("Name");
            listOfAtts.add("Type");
            listOfAtts.add("MinLat");
            listOfAtts.add("MaxLat");
            listOfAtts.add("MinLong");
            listOfAtts.add("MaxLong");
            ScanResult spotScanResult = ddbClient.scan(SPOT_TABLE_NAME, listOfAtts);
            List<Map<String, AttributeValue>> items = spotScanResult.getItems();
            List<Spot> spotList = new ArrayList<Spot> ();
            for (Map<String, AttributeValue> item:items) {
                AttributeValue idAtt = item.get("Id");
                String id = idAtt.getS();
                AttributeValue nameAtt = item.get("Name");
                String name = nameAtt.getS();
                AttributeValue typeAtt = item.get("Type");
                String type = typeAtt.getS();
                AttributeValue minLatAtt = item.get("MinLat");
                Double minLat = Double.parseDouble(minLatAtt.getN());
                AttributeValue maxLatAtt = item.get("MaxLat");
                Double maxLat = Double.parseDouble(maxLatAtt.getN());
                AttributeValue minLongAtt = item.get("MinLong");
                Double minLong = Double.parseDouble(minLongAtt.getN());
                AttributeValue maxLongAtt = item.get("MaxLong");
                Double maxLong = Double.parseDouble(maxLongAtt.getN());

                SpotLocation location = new SpotLocation(minLat, minLong, maxLat, maxLong);
                Spot spot = SpotFactory.createSpot(id, name, location, type);

                spotList.add(spot);

            }

            return spotList;
        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return Collections.<Spot>emptyList();
        }
    }

    public List<Spot> loadSortedSpots(Context context, SpotKey key, double range){
        List<Spot> spots = loadSpots(context);
        List<Spot> sortedList = filterAndSortResultByLocation(spots, key.getCurrentLatitude(), key.getCurrentLongitude(), range);
        return sortedList;
    }

    private List<Spot> filterAndSortResultByLocation(List<Spot> result, double currentLatitude, double currentLongitude, double range) {
        List<Spot> nearbySpots = new ArrayList<Spot>();
        for (Spot spot:result) {
            if (spot.getLocation().isInRange(currentLatitude,currentLongitude, range))
            {
                nearbySpots.add(spot);
            }
        }
        // TODO TALYAC - if the size of the list is too large filter recursively
        // TODO TALYAC - list should also be sorted
         Collections.sort(nearbySpots, new SpotDistanceComparator(currentLatitude,currentLongitude));
        return nearbySpots;
    }

    public List<MiniApp> loadMiniApps(Context context, Spot spot) {
        try {
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);
            List<String> listOfAtts = new ArrayList<>();
            listOfAtts.add("Id");
            listOfAtts.add("Name");
            listOfAtts.add("Description");
            listOfAtts.add("Icon");
            listOfAtts.add("Operation");
            listOfAtts.add("Params");
            listOfAtts.add("UpVotes");
            listOfAtts.add("DownVotes");

            Map<String, AttributeValue> expressionAttributeValues =
                    new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":idValue", new AttributeValue().withS(spot.getId()));

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName("MiniApp")
                    .withFilterExpression("SpotId = :idValue")
                    .withProjectionExpression(null) // this will bring all attributes
                    .withExpressionAttributeValues(expressionAttributeValues);
            ScanResult miniAppScanResult = ddbClient.scan(scanRequest);
            List<Map<String, AttributeValue>> items = miniAppScanResult.getItems();
            List<MiniApp> miniAppList = new ArrayList<MiniApp> ();
            for (Map<String, AttributeValue> item:items) {
                AttributeValue idAtt = item.get("Id");
                String id = idAtt.getS();
                AttributeValue nameAtt = item.get("Name");
                String name = nameAtt.getS();
                AttributeValue descAtt = item.get("Description");
                String desc = descAtt.getS();
                AttributeValue iconAtt = item.get("Icon");
                String icon = iconAtt.getS();
                AttributeValue operationAtt = item.get("Operation");
                String operation = operationAtt.getS();
                AttributeValue paramsAtt = item.get("Params");
                String params = paramsAtt.getS();
                AttributeValue upVotesAtt = item.get("UpVotes");
                Integer upVotes = Integer.valueOf(upVotesAtt.getN());
                AttributeValue downVotesAtt = item.get("DownVotes");
                Integer downVotes = Integer.valueOf(downVotesAtt.getN());

                MiniApp miniApp = MiniAppFactory.createMiniApp(context, id, name, desc, icon, operation, params, spot, upVotes, downVotes);

                miniAppList.add(miniApp);

            }

            return miniAppList;
        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return Collections.<MiniApp>emptyList();
        }

    }

    public List<MiniApp> loadSortedMiniApps(Context context, Spot spot) {
        List<MiniApp> miniApps = loadMiniApps(context, spot);
        Collections.sort(miniApps, new MiniAppVotesComparator());
        return miniApps;
    }

    public boolean saveNewSpot(Context context, Spot spot) {
        try {
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);

            Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
            item.put("Id", new AttributeValue().withS(spot.getId()));
            item.put("Name", new AttributeValue(spot.getName()));
            item.put("Type", new AttributeValue(spot.getType()));
            item.put("MinLat", new AttributeValue().withN(Double.toString(spot.getLocation().getMinLat())));
            item.put("MaxLat", new AttributeValue().withN(Double.toString(spot.getLocation().getMaxLat())));
            item.put("MinLong", new AttributeValue().withN(Double.toString(spot.getLocation().getMinLong())));
            item.put("MaxLong", new AttributeValue().withN(Double.toString(spot.getLocation().getMaxLong())));


    // Construct a map of expected current values for the conditional write
    //        Map<String, ExpectedAttributeValue> expected = new HashMap<String, ExpectedAttributeValue>();
    //        expected.put("Price", new ExpectedAttributeValue()
    //                .withValue(new AttributeValue().withN("26")));

    // Make the request
            PutItemResult result = ddbClient.putItem(new PutItemRequest()
                    .withTableName(SPOT_TABLE_NAME)
                    .withItem(item)
                    //.withExpected(expected)
                    .withReturnValues(ReturnValue.ALL_OLD));
            /////////////////////////////////////////////////////

        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
        generateMiniAppsForNewSpot(context, spot);
        return true;
    }

    private void generateMiniAppsForNewSpot(Context context, Spot spot) {
        String spotType = spot.getType();
        MiniAppGenerator miniAppGenerator = MiniAppGeneratorFactory.createMiniAppGenerator(spotType);
        List<MiniApp> miniApps = miniAppGenerator.generateMiniApps();
        for (MiniApp miniApp:miniApps) {
            saveNewMiniApp(context, miniApp);
        }

    }

    public boolean
    saveNewMiniApp(Context context, MiniApp miniApp) {
        try {
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);

            Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
            item.put("Id", new AttributeValue().withS(miniApp.getId()));
            item.put("SpotId", new AttributeValue().withS(miniApp.getSpot().getId()));
            item.put("Description", new AttributeValue().withS(miniApp.getDescription()));
            item.put("Icon", new AttributeValue().withS(miniApp.getIcon()));
            item.put("Name", new AttributeValue(miniApp.getName()));
            item.put("Operation", new AttributeValue(miniApp.getOperation()));
            item.put("Params", new AttributeValue(miniApp.getParams()));
            item.put("UpVotes", new AttributeValue().withN(Integer.toString(miniApp.getUpVotes())));
            item.put("DownVotes", new AttributeValue().withN(Integer.toString(miniApp.getDownVotes())));


            // Make the request
            PutItemResult result = ddbClient.putItem(new PutItemRequest()
                    .withTableName(MINI_APP_TABLE_NAME)
                    .withItem(item)
                    //.withExpected(expected)
                    .withReturnValues(ReturnValue.ALL_OLD));
        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateSpotLocation(Context context, Spot spot) {
        try{
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);

            HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
            key.put("Id", new AttributeValue().withS(spot.getId()));


            Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":minLat", new AttributeValue().withN(Double.toString(spot.getLocation().getMinLat())));
            expressionAttributeValues.put(":maxLat", new AttributeValue().withN(Double.toString(spot.getLocation().getMaxLat())));
            expressionAttributeValues.put(":minLong", new AttributeValue().withN(Double.toString(spot.getLocation().getMinLong())));
            expressionAttributeValues.put(":maxLong", new AttributeValue().withN(Double.toString(spot.getLocation().getMaxLong())));

            ReturnValue returnValues = ReturnValue.ALL_NEW;

            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName(SPOT_TABLE_NAME)
                    .withKey(key)
                    .withUpdateExpression("set MinLat = :minLat, MaxLat = :maxLat, MinLong = :minLong, MaxLong = :maxLong")
    //                .withConditionExpression("Price = :val2")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withReturnValues(returnValues);

            UpdateItemResult updateItemResult = ddbClient.updateItem(updateItemRequest);

        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateMiniAppVotes(Context context, MiniApp miniApp) {
        try{
            AmazonDynamoDB ddbClient = createAmazonDynamoDB(context);

            HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
            key.put("Id", new AttributeValue().withS(miniApp.getId()));
            key.put("SpotId", new AttributeValue().withS(miniApp.getSpot().getId()));


            Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":upVotes", new AttributeValue().withN(Integer.toString(miniApp.getUpVotes())));
            expressionAttributeValues.put(":downVotes", new AttributeValue().withN(Integer.toString(miniApp.getDownVotes())));

            ReturnValue returnValues = ReturnValue.ALL_NEW;

            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName(MINI_APP_TABLE_NAME)
                    .withKey(key)
                    .withUpdateExpression("set UpVotes = :upVotes, DownVotes = :downVotes")
    //                .withConditionExpression("Price = :val2")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withReturnValues(returnValues);

            UpdateItemResult updateItemResult = ddbClient.updateItem(updateItemRequest);
        } catch (AmazonServiceException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }

        return true;
    }
}
