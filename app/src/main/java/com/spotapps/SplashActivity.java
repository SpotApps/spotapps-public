package com.spotapps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.spotapps.beans.SpotBundle;
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import com.spotapps.beans.SpotLocation;
import com.spotapps.sensors.SensorsFacade;
import com.spotapps.server.GetSpotsTask;

import java.util.concurrent.ExecutionException;

/**
 * SplashActivity is the first activity in the application - it is responsible for opening a "loading..." splash screen.
 * Then in the background it loads the current spot and launches the relevant page in the app.
 * The entire lifetime of an activity happens between the first call to onCreate(Bundle)
 * through to a single final call to onDestroy().
 * An activity will do all setup of "global" state in onCreate(), and release all remaining resources in onDestroy().
 * For example, if it has a thread running in the background to download data from the network,
 * it may create that thread in onCreate() and then stop the thread in onDestroy().
 * from https://developer.android.com/reference/android/app/Activity.html
 */
public class SplashActivity extends Activity {

    private GetSpotsTask aSyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        final TextView tv = (TextView) findViewById(R.id.splashtextid);
        final ImageView iv = (ImageView) findViewById(R.id.splashimage);
        final AnimationSet an = (AnimationSet)AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotation);
        final Animation anFade = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);
        iv.startAnimation(an);
        for (Animation animation : an.getAnimations()) {

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    startLoadingSpots();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    if (hasLoadingEnded()) {

//                        Bundle bundle = new Bundle();

//                        try {
//                            SpotBundle spots = loadSpotsForCurrentLocation();
//                            SpotBundleUtils.storeSpotsInBundle(bundle, spots);
//                        } catch (ExecutionException|InterruptedException e) {
//                            logMessage(e);
//                            return; // TODO end application?
//                        }
                        iv.startAnimation(anFade);
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
//                        i.putExtras(bundle); // TODO TALYAC can this work without loading spots here?
                        startActivity(i);
                        finish(); // this prevents the user from returning to the splash screen
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        aSyncTask.cancel(true);
    }

    private void logMessage(Exception e) {
        // TODO: 12/30/2015
    }

//    int loadingCounter = 0;
    //int maxLoaded = 5;
    private void startLoadingSpots() {
        // TODO: 12/30/2015 create key based on location data
//        SpotLocation currentLocation = SensorsFacade.getNewLocation();
//        SpotKey spotKey = SpotFactory.createSpotKey(currentLocation.getMinLat(), currentLocation.getMinLong());
//        aSyncTask = new GetSpotsTask(this);
//        aSyncTask.execute(spotKey);

    }

    private boolean hasLoadingEnded() {
        //return aSyncTask.getLoadedSpot() != null;
//        AsyncTask.Status status = aSyncTask.getStatus();
//        return AsyncTask.Status.FINISHED.equals(status);
        return true;
    }

//    private SpotBundle loadSpotsForCurrentLocation() throws ExecutionException, InterruptedException {
//
//        return aSyncTask.get();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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