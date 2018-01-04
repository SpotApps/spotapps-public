package com.spotapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spotapps.beans.FullSpot;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotBundle;
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import com.spotapps.beans.SpotLocation;
import com.spotapps.logic.UpdateSpotResult;
import com.spotapps.logic.UpdateSpotTask;
import com.spotapps.sensors.SensorsFacade;
import com.spotapps.server.AWSAdapter;
import com.spotapps.server.ServerFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ViewNearbySpotsActivity extends Activity {

    private SaveSpotTask aSyncTask;
    private UpdateSpotTask updateSpotTask;

    private SpotBundle spotBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_spots);

        Bundle bundle = getIntent().getExtras();
        spotBundle= SpotBundleUtils.loadSpotsFromBundle(bundle);
        //filterAnd Sort Results like in GetSpotsTask
        List<Spot> spots = spotBundle.getSpots();
        populateSpots(spots);

        registerOnClickCallback();

        Spinner spinner = (Spinner) findViewById(R.id.SpotTypeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spot_types_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//
//                String spotType = adapterView.getAdapter().getItem(position).toString();
////                TextView textView = (TextView) findViewById(R.id.editType);
////                textView.setText(spotType);
//                // TODO TALYAC not sure I want this
////                replaceMiniAppTypeParamEditor(miniAppType);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        // TODO TALYAC if we want we can start a background service like this
        // but we shouldn't start it here to avoid blocking the ui thread.
        // instead use a background thread or an asynchronous response mechanism
        //        Intent service = new Intent(context, MyService.class);
        //        startService(service);
    }

    private void populateSpots(List<Spot> nearbySpots) {
        // array of options - > ArrayAdapter -> ListView
        // TODO TALYAC here we should load the spots relevant to the current spot
        // create list of items


        // create adapter from list
        ArrayAdapter<Spot> adapter = new ArrayAdapter<Spot>(this,
                R.layout.spotlistview_item_row, // layout to use
                R.id.textViewWebLinkName,
                nearbySpots); // items in list

        // configure the list view
        final ListView nearbySpotListView = (ListView) findViewById(R.id.nearbySpotListView);
        nearbySpotListView.setAdapter(adapter);
    }

    private void registerOnClickCallback() {
        final ListView nearbySpotListView = (ListView) findViewById(R.id.nearbySpotListView);
        nearbySpotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Object o = nearbySpotListView.getItemAtPosition(position);
                Spot chosenSpot = (Spot)o;
                // TextView textView = (TextView) viewClicked;
                // textView.getText().toString()
                String text = "You clicked on " + chosenSpot.getName();
                Toast.makeText(ViewNearbySpotsActivity.this, text, Toast.LENGTH_LONG).show();
                updateSpotWithNewLocation(chosenSpot, spotBundle.getKey().getCurrentLatitude(), spotBundle.getKey().getCurrentLongitude());

            }


        });
    }

    private void updateSpotWithNewLocation(Spot chosenSpot, double newLatitude, double newLongitude) {

        Spot newSpot = SpotFactory.updateSpotLocation(chosenSpot, newLatitude, newLongitude);
        if (updateSpotTask == null){ // TODO how do I rerun this and save only once if clicked twice.
            updateSpotTask = new UpdateSpotTask(this){
                protected void onPostExecute(UpdateSpotResult updateSpotResult) {
                    // TODO TALYAC update the adapter with the new data
//          populateMiniApps(result);
//          registerOnClickCallback();
                    spotBundle.updateFirstSpot(updateSpotResult.getSpot());
                    openMainActivity(updateSpotResult.getSpot());
                }
            };
            updateSpotTask.execute(newSpot);
        }

        //openMainActivity(); //TODO this should be done in the postExecute after the spot has actually been updated
    }

    private void openMainActivity(FullSpot spot) {
        Bundle bundle = new Bundle();
        SpotBundleUtils.storeSpotsInBundle(bundle, spotBundle);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", spot);
        setResult(RESULT_OK, returnIntent);
        finish();


    }


    public void createNewSpot(View view) {
        EditText nameField =  (EditText)findViewById(R.id.editName);
        String name = nameField.getText().toString();

        Spinner spotTypeSpinner = (Spinner) findViewById(R.id.SpotTypeSpinner);
        String typeSpinner = spotTypeSpinner.getSelectedItem().toString();

        String text = "Saving spot " + name + " of type " + typeSpinner;
        Toast.makeText(ViewNearbySpotsActivity.this, text, Toast.LENGTH_LONG).show();
        // TODO TALYAC temp until I see it works
//        EditText typeField =  (EditText)findViewById(R.id.editType);
//        String type = typeField.getText().toString();

        //Intents start another activity
        // TODO TALYAC start new service that creates new spot?
        // they all extend content and they are all part of the same context (like springmanager)
//        Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
//        EditText et = (EditText) findViewById(R.id.edit_miniapp_name);
//        String msg = et.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, msg);
//        startActivity(intent);

        // according to type determine size of spot

//        SpotLocation location = //SensorsFacade.getNewLocation();
//        SpotFactory.spotBundle.getKey().getCurrentLatitude();
//        spotBundle.getKey().getCurrentLongitude();
        String id = UUID.randomUUID().toString();
        SpotKey key = spotBundle.getKey();
        Spot spot = SpotFactory.createNewSpot(id, name, key.getCurrentLatitude(), key.getCurrentLongitude(), typeSpinner);
        if (SpotFactory.isValidSpot(spot) && aSyncTask == null){ // TODO how do I rerun this and save only once if clicked twice.
            aSyncTask = new SaveSpotTask(this);
            aSyncTask.execute(spot);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_nearby_spots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SaveSpotTask extends AsyncTask<Spot, Integer, Boolean> {

        public SaveSpotTask(Context context) {
            this.context = context;
        }

        private Context context;

        protected Boolean doInBackground(Spot... keys) {
        /*
            To ensure that a task is cancelled as quickly as possible,
            you should always check the return value of isCancelled() periodically from doInBackground(Object[]),
            if possible (inside a loop for instance.)
            if possible (inside a loop for instance.)
         */
            // TODO extract location data from key
            Spot key = keys[0];
            if (key == null) {
                throw new RuntimeException("AHHHHHHHH!!!!!");
            }
            AWSAdapter awsAdapter = new AWSAdapter();
            boolean result = awsAdapter.saveNewSpot(this.context, key);
            // publishProgress(50);
            // TODO TALYAC here we can call publishProgress()

            //TODO TALYAC shouldn't this be onPostExecute
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", key);
            if (result) {
                setResult(RESULT_OK, returnIntent);
            } else {
                setResult(RESULT_CANCELED, returnIntent);
                String text = "Failed to save spot " + key.getName();
                Toast.makeText(ViewNearbySpotsActivity.this, text, Toast.LENGTH_LONG).show();
            }
            finish();
            return result;
        }

        // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
        protected void onProgressUpdate(Integer... progress) {
//        if (progress != null && progress.length > 0){
//            setLoadingCounter(progress[0]);
//        }
        }

        protected void onPostExecute(Boolean result) {
            // TODO TALYAC update the adapter with the new data
//          populateMiniApps(result);
//          registerOnClickCallback();
        }
    }


}
