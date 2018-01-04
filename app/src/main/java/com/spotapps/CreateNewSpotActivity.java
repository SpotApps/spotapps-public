package com.spotapps;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotLocation;
import com.spotapps.sensors.SensorsFacade;
import com.spotapps.server.AWSAdapter;
import com.spotapps.server.ServerFacade;

import java.util.List;


public class CreateNewSpotActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_spot);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_spot, menu);
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

    public void createNewSpot(View view) {
        //Intents start another activity
        // TODO TALYAC start new service that creates new spot?
        // they all extend content and they are all part of the same context (like springmanager)
//        Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
//        EditText et = (EditText) findViewById(R.id.edit_miniapp_name);
//        String msg = et.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, msg);
//        startActivity(intent);

        String text = "You saved a spot ";
        Toast.makeText(CreateNewSpotActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
