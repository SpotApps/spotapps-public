package com.spotapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.spotapps.beans.FullSpot;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppTypes;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotBundle;
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import com.spotapps.logic.MiniAppOperation;
import com.spotapps.logic.UpdateMiniAppTask;
import com.spotapps.server.AWSAdapter;

import java.util.List;

/**
 * MainActivity shows a single spot, the available miniapps at that spot and buttons to move to other activities:
 * "Create new miniapp" and "I'm not on this spot".
 * used to be ActionBarActivity
 */
public class MainActivity extends Activity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 1000;
    private static final long LOCATION_WAIT_TIME = 60000;
    public static final String LOG_TAG = "SpotAppsMain";

    // the state of the activity is stored here
    private SpotBundle spotBundle;

    // used to access location
    private GoogleApiClient apiClient;

    // inline tasks
    private GetSpotBundleTask getSpotsTask;
    private UpdateMiniAppTask updateMiniAppTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spotBundle = null; //initialize state
        if (!isOnline()){
            handleFailedConnection("Please check the network connection");
            // TODO TALYAC change this to handle the error better. Should be consistent with how the app handles location as well
            return;
        }
        if (savedInstanceState != null) {
            // Restore value of members from saved state - this will be used when the screen is rotated, etc
            spotBundle = SpotBundleUtils.loadSpotsFromBundle(savedInstanceState);
            if (spotBundle != null) {
                if  (Log.isLoggable(LOG_TAG, Log.DEBUG)){
                    Log.d(LOG_TAG, "savedInstanceState has: "+spotBundle.toString());
                }
            }
        }
        if (spotBundle == null) {
            // the activity is opened from viewNearbySpots or createMiniApp
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                spotBundle = SpotBundleUtils.loadSpotsFromBundle(bundle);
                if (spotBundle != null) {
                    if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                        Log.d(LOG_TAG, "the extras on the bundle have: " + spotBundle.toString());
                    }
                }

            }
        }
        if (spotBundle != null) {
            populateUIUsingSpot(spotBundle);
            /////////////////// TODO NOOOOOO
            append("<bundle>");
        }
    // Create an instance of GoogleAPIClient to access location.
        else if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


//        ProgressDialog progress = new ProgressDialog(this);
//        progress.setMessage("In a moment I'll tell you what's here!");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//
//        progress.setProgress(0);
//        progress.show();
//        final int totalProgressTime = 100;
//        final Thread t = new Thread() {
//            @Override
//            public void run() {
//                int jumpTime = 0;
//
//                while(jumpTime < totalProgressTime) {
//                    try {
//                        sleep(200);
//                        jumpTime += 5;
//                        //progress.setProgress(jumpTime);
//                    }
//                    catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        t.start();/

//        Spot spot = ServerFacade.loadSpot();


        // TODO TALYAC if we want we can start a background service like this
        // but we shouldn't start it here to avoid blocking the ui thread.
        // instead use a background thread or an asynchronous response mechanism
        //        Intent service = new Intent(context, MyService.class);
        //        startService(service);

    }

    private void handleFailedConnection(String msg) {
        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        String formattedMsg = "Connection failed: " + msg;
        spotTitleTextView.setText(msg);
        if  (Log.isLoggable(LOG_TAG, Log.ERROR)){

            Log.e(LOG_TAG, formattedMsg);
        }
    }

    private void handleFailedLocation(String msg) {
        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        String formattedMsg = "Location could not be found: " + msg;
        spotTitleTextView.setText(msg);
        if  (Log.isLoggable(LOG_TAG, Log.ERROR)){

            Log.e(LOG_TAG, formattedMsg);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SpotBundleUtils.storeSpotsInBundle(outState, spotBundle);
    }

    protected void onStart() {
        if (apiClient!= null) {
            apiClient.connect();
        }
        super.onStart();
    }

    protected void onStop() {
        // TODO TALYAC cleanup anything necessary - only onDestroy is called if finish is invoked during onCreate
        if (apiClient!= null) {
            apiClient.disconnect();
        }
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getSpotsTask != null) {
            getSpotsTask.cancel(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO TALYAC cleanup anything necessary
    }

    // this is called when google play services connects.
    // This is a good time to get the last know location
    @Override
    public void onConnected(Bundle bundle) {
// TODO: 12/30/2015 create key based on location data
        refreshSpots();
        /////////////////// TODO NOOOOOO
        append("<connected>");
    }

    private void append(String msg) {
        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        String formattedMsg = spotTitleTextView.getText() + msg;
        spotTitleTextView.setText(formattedMsg);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        handleFailedConnection("Unable to load Spot without a location. Please check the GPS");
        Log.e(LOG_TAG,"TODO: error message about connection failing!");
    }


    private void refreshSpots() {
        // if we have permissions load the spots. Otherwise request permission and if granted load the spots
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // this will be called from onRequestPermissionsResult as well in case we don't have permissions yet
            /////////////////// TODO NOOOOOO
            append("<permitted>");
            loadSpotsForLastKnownLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /////////////////// TODO NOOOOOO
                    append("<not perm>");
                    // permission was granted, yay!
                    loadSpotsForLastKnownLocation();

                } else {
                    handleFailedLocation("Location permissions required to load spots");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void loadSpotsForLastKnownLocation() {
        //get the last know location
        try {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            /////////////////// TODO NOOOOOO
            append("<long"+lastLocation.getLongitude() + ">");
            append("<lat"+lastLocation.getLatitude() + ">");
            if (lastLocation != null) {
                SpotKey spotKey = SpotFactory.createSpotKey(lastLocation);
                getSpotsTask = new GetSpotBundleTask(MainActivity.this);
                getSpotsTask.execute(spotKey);
            } else {
                // if no last known location - wait for the next one
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setMaxWaitTime(LOCATION_WAIT_TIME);
                locationRequest.setExpirationDuration(LOCATION_WAIT_TIME);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(UPDATE_INTERVAL);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                                SpotKey spotKey = SpotFactory.createSpotKey(location);
                                getSpotsTask = new GetSpotBundleTask(MainActivity.this);
                                getSpotsTask.execute(spotKey);
                                LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
                            }


                        }

                );
            }
        }catch (SecurityException se){
            // we did not get permissions?
            handleFailedLocation("Location permissions required to load spots");
        }
    }

    private void populateUIUsingSpot(SpotBundle spotBundle) {                                                                           
        if (spotBundle != null) {
            FullSpot firstSpot = spotBundle.getFirstSpot();
            if (firstSpot != null) {

                // TODO deleted for debugging populateSpot(firstSpot);
                ///////////////

                final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
                append(firstSpot.getName().substring(0, 9) + ": " + spotBundle.getKey().getCurrentLongitude());
                /////////////////// TODO NOOOOOO
                append("<loclist>");
                /////////////////



                List<MiniApp> miniApps = firstSpot.getMiniApps();
                populateMiniApps(miniApps);
                registerOnClickCallback();
            }
        }
    }

    private void populateSpot(Spot spot) {
        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        spotTitleTextView.setText(spot.getName());
    }


    private void populateMiniApps(List<MiniApp> miniApps) {
        // array of options - > ArrayAdapter -> ListView
        // TODO TALYAC here we should load the miniapps relevant to the current spot
        // create list of items
        // TODO TALYAC use a fragment for the list view and inflate when onCreate?

        // create adapter from list
        ArrayAdapter<?> adapter = new SpotAdapter(this, miniApps); // items in list

        // configure the list view
        final ListView miniAppRatedListView = (ListView) findViewById(R.id.miniAppRatedListView);
        miniAppRatedListView.setAdapter(adapter);
    }

    private void registerOnClickCallback() {
        final ListView miniAppRatedListView = (ListView) findViewById(R.id.miniAppRatedListView);
        miniAppRatedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Object o = miniAppRatedListView.getItemAtPosition(position);
                MiniApp chosenMiniApp = (MiniApp) o;
                String text = R.string.lbl_toast_executing_operation + chosenMiniApp.getDescription();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                callOperation(chosenMiniApp);
            }
        });
    }

    private void callOperation(MiniApp miniApp) {

        MiniAppOperation miniAppOperation = new MiniAppOperation(miniApp, this);
        Intent miniAppIntent = miniAppOperation.createIntentByOperation();
        startActivity(miniAppIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
            CharSequence text = spotTitleTextView.getText();
            Toast.makeText(MainActivity.this, "title"+text, Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_refresh) {

            /////////////////////// TODO NNNNNNNNNNNNNNNNNNNNNNOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!

            if (apiClient == null) {
                apiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            } else {
                // if we are already connected
                refreshSpots();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewMiniApp(View view) {
        //Intents start another activ..ity
        // they all extend content and they are all part of the same context (like springmanager)
        if (spotBundle != null) {

            Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
            Bundle bundle = new Bundle();
            SpotBundleUtils.storeSpotsInBundle(bundle, spotBundle);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                MiniApp miniApp = (MiniApp)data.getSerializableExtra("result");
                // TODO TALYAC refreshSpots();

            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    public void viewNearbySpots(View view) {
        // TODO TALYAC can I reuse the bundle?
        if (spotBundle != null){
            Bundle bundle = new Bundle();
            SpotBundleUtils.storeSpotsInBundle(bundle, spotBundle);
            Intent intent = new Intent(this, ViewNearbySpotsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }






    /**
     * Created by tty on 4/7/2016.
     * TODO TALYAC this should use GetTask and the url should use UriBuilder (https://www.udacity.com/course/viewer#!/c-ud853/l-1469948762/e-1630778633/m-1630778636)
     */

    public class GetSpotBundleTask extends AsyncTask<SpotKey, Integer, SpotBundle> {

        private static final double RANGE_FOR_SEARCH = 100;//0.5d;
        public GetSpotBundleTask(Context context) {
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
            if (key == null) {
                throw new RuntimeException("AHHHHHHHH!!!!!");
            }

            AWSAdapter awsAdapter = new AWSAdapter();
            List<Spot> spots = awsAdapter.loadSortedSpots(this.context, key, RANGE_FOR_SEARCH);
            // publishProgress(50);
            // TODO TALYAC here we can call publishProgress() while filtering the spots
            StringBuilder paramString = new StringBuilder("list of miniApps:\n");

            // load the mini apps for the first spot
            SpotBundle spotBundle = null;
            if (spots.size() > 0) {
                Spot loadedSpot = spots.get(0);
                List<MiniApp> listMiniApp = awsAdapter.loadSortedMiniApps(this.context, loadedSpot);
                FullSpot firstSpot = SpotFactory.createNewFullSpot(loadedSpot, listMiniApp);
                spotBundle = new SpotBundle(key, spots, firstSpot, SpotBundle.BundleStatus.LOADED);
                for (MiniApp miniApp:listMiniApp) {
                    paramString.append(miniApp.getName()).append(" :").append(miniApp.getParams()).append(" /\n");
                }
                if  (Log.isLoggable(LOG_TAG, Log.DEBUG)){
                    Log.d(LOG_TAG, paramString.toString());
                }
            }

            return spotBundle;
        }


        // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
        protected void onProgressUpdate(Integer... progress) {
//        if (progress != null && progress.length > 0){
//            setLoadingCounter(progress[0]);
//        }
        }

        protected void onPostExecute(SpotBundle result) {
            // TODO TALYAC update the adapter with the new data
            spotBundle = result;
            /////////////////// TODO NOOOOOO
            append("<get>");
            populateUIUsingSpot(spotBundle);
        }
    }

    private class SpotAdapter extends ArrayAdapter {
        private LayoutInflater inflater;
        public SpotAdapter(MainActivity mainActivity, List<MiniApp> miniApps) {
            super(mainActivity,
                    R.layout.listview_item_row, // layout to use
                    R.id.textViewWebLinkName,miniApps);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView holder = null;
            int type = getItemViewType(position);
            MiniApp miniApp = (MiniApp) getItem(position);


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_item_row, null);

            } else {
                // TODO TALYAC where did this code come from? holder = (TextView) convertView.getTag();
            }
            holder = (TextView)convertView.findViewById(R.id.textViewWebLinkName);

            TextView description = (TextView)convertView.findViewById(R.id.textViewWebLinkDescription);
            description.setText(getTextOfMiniAppType(miniApp.getOperation()) + " " + miniApp.getParams());

            MiniAppOperation miniAppOperation = new MiniAppOperation(miniApp, MainActivity.this);
            ImageView iv = (ImageView)convertView.findViewById(R.id.imageViewWebLinkIcon);
            miniAppOperation.modifyIconByOperation(iv);

            convertView.setTag(holder);
            holder.setText(miniApp.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View row) {
                    final ListView miniAppRatedListView = (ListView) findViewById(R.id.miniAppRatedListView);
                    final int position = miniAppRatedListView.getPositionForView(row);
                    if (position != ListView.INVALID_POSITION) {
                        Object o = miniAppRatedListView.getItemAtPosition(position);
                        MiniApp chosenMiniApp = (MiniApp) o;
                        // TextView textView = (TextView) viewClicked;
                        // textView.getText().toString()
                        String text = "You clicked on " + chosenMiniApp.getDescription();
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                        callOperation(chosenMiniApp);
                        updateVotes(chosenMiniApp, 1);
                    }
                }
            });

            ImageButton btnUpVote = (ImageButton)convertView.findViewById(R.id.btn_thumbs_up);
            btnUpVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View row) {
                    final ListView miniAppRatedListView = (ListView) findViewById(R.id.miniAppRatedListView);
                    final int position = miniAppRatedListView.getPositionForView(row);
                    if (position != ListView.INVALID_POSITION) {
                        Object o = miniAppRatedListView.getItemAtPosition(position);
                        MiniApp chosenMiniApp = (MiniApp) o;
                        // TextView textView = (TextView) viewClicked;
                        // textView.getText().toString()
                        String text = "You upvoted " + chosenMiniApp.getName();
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                        updateVotes(chosenMiniApp, 10);
                    }
                }
            });
            ImageButton btnDownVote = (ImageButton)convertView.findViewById(R.id.btn_thumbs_down);
            btnDownVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View row) {
                    final ListView miniAppRatedListView = (ListView) findViewById(R.id.miniAppRatedListView);
                    final int position = miniAppRatedListView.getPositionForView(row);
                    if (position != ListView.INVALID_POSITION) {
                        Object o = miniAppRatedListView.getItemAtPosition(position);
                        MiniApp chosenMiniApp = (MiniApp) o;
                        // TextView textView = (TextView) viewClicked;
                        // textView.getText().toString()
                        String text = "You downvoted " + chosenMiniApp.getName();
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                        updateVotes(chosenMiniApp, -10);
                    }
                }
            });


            return convertView;
        }

        private String getTextOfMiniAppType(String operation) {
            MiniAppTypes miniAppTypes = MiniAppTypes.lookup(operation);
            switch (miniAppTypes) {
                case DIAL: return getText(R.string.op_call).toString();
                case OPEN_APP: return getText(R.string.op_open_app).toString();
                default: return getText(R.string.op_open_url).toString();
            }
        }

        private void updateVotes(MiniApp miniApp, int votes) {
            if (votes > 0) {
                miniApp.addUpVotes(votes);
            }
            else {
                miniApp.addDownVotes(votes);
            }
            if (updateMiniAppTask == null){ // TODO how do I rerun this and save only once if clicked twice.
                updateMiniAppTask = new UpdateMiniAppTask(MainActivity.this);
                updateMiniAppTask.execute(miniApp);
            }
        }

    }


}
