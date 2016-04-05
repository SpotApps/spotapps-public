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
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.server.ServerFacade;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * MainActivity is shows the current spot. the available miniapps at that spot and buttons to move to other activities.
 * "Create new miniapp". And "I'm not on this spot".
 */
public class MainActivity extends ActionBarActivity {


//    public final static String EXTRA_MESSAGE = "com.spotapps.msg";

    public void createNewMiniApp(View view) {
        //Intents start another activity
        // they all extend content and they are all part of the same context (like springmanager)
        Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
//        EditText et = (EditText) findViewById(R.id.edit_miniapp_name);
//        String msg = et.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

    public void viewNearbySpots(View view) {
        // TODO TALYAC can I reuse the bundle?
        Bundle bundle = new Bundle();
        Bundle mainBundle = getIntent().getExtras();
        List<Spot> spots = SpotBundleUtils.loadSpotsFromBundle(mainBundle);
        SpotBundleUtils.storeSpotsInBundle(bundle, spots);
        Intent intent = new Intent(this, ViewNearbySpotsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // the splash activity loaded this bundle when the app started for the first time
        Bundle bundle = getIntent().getExtras();
        List<Spot> spots = SpotBundleUtils.loadSpotsFromBundle(bundle);



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
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        t.start();/

//        Spot spot = ServerFacade.loadSpot();
        populateSpot(spots.get(0));

        List<MiniApp> miniApps = ServerFacade.loadMiniApps(spots.get(0));
        populateMiniApps(miniApps);
        registerOnClickCallback();

        // TODO TALYAC if we want we can start a background service like this
        // but we shouldn't start it here to avoid blocking the ui thread.
        // instead use a background thread or an asynchronous response mechanism
        //        Intent service = new Intent(context, MyService.class);
        //        startService(service);

    }

    private void populateSpot(Spot spot) {
        final TextView spotTitleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        spotTitleTextView.setText(spot.getName());
    }


    private void populateMiniApps(List<MiniApp> miniApps) {
        // array of options - > ArrayAdapter -> ListView
        // TODO TALYAC here we should load the miniapps relevant to the current spot
        // create list of items


        // create adapter from list
        ArrayAdapter<MiniApp> adapter = new ArrayAdapter<MiniApp>(this,
                R.layout.listview_item_row, // layout to use
                R.id.textViewWebLinkName,
                miniApps); // items in list

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
                // TextView textView = (TextView) viewClicked;
                // textView.getText().toString()
                String text = "You clicked on " + chosenMiniApp.getDescription();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // TODO TALYAC cleanup anything necessary
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO TALYAC cleanup anything necessary - only onDestroy is called if finish is invoked during onCreate
    }

    private void logMessage(Exception e) {
        // TODO: 12/30/2015
    }
}
