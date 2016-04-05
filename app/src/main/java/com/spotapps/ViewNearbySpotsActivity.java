package com.spotapps;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotapps.beans.MiniApp;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotLocation;
import com.spotapps.sensors.SensorsFacade;
import com.spotapps.server.ServerFacade;

import java.util.ArrayList;
import java.util.List;


public class ViewNearbySpotsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_spots);

        Spot spot = ServerFacade.loadSpot();

        populateSpot(spot);

        SpotLocation spotLocation = SensorsFacade.getCurrentLocation();
        List<Spot> spots = ServerFacade.loadSpots(spotLocation);
        populateSpots(spots);

        registerOnClickCallback();

        // TODO TALYAC if we want we can start a background service like this
        // but we shouldn't start it here to avoid blocking the ui thread.
        // instead use a background thread or an asynchronous response mechanism
        //        Intent service = new Intent(context, MyService.class);
        //        startService(service);
    }

    private void populateSpot(Spot spot) {
//        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
//        spotTitleTextView.setText(spot.getName());
    }


    private void populateSpots(List<Spot> nearbySpots) {
        // array of options - > ArrayAdapter -> ListView
        // TODO TALYAC here we should load the spots relevant to the current spot
        // create list of items


        // create adapter from list
        ArrayAdapter<Spot> adapter = new ArrayAdapter<Spot>(this,
                R.layout.listview_item_row, // layout to use
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
            }
        });
    }


    public void createNewSpot(View view) {
        //Intents start another activity
        // TODO TALYAC start new service that creates new spot?
        // they all extend content and they are all part of the same context (like springmanager)
//        Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
//        EditText et = (EditText) findViewById(R.id.edit_miniapp_name);
//        String msg = et.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, msg);
//        startActivity(intent);
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
}
