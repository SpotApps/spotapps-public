package com.spotapps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppFactory;
import com.spotapps.beans.MiniAppTypes;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotBundle;
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.logic.EditMiniAppOperationFragmentListener;
import com.spotapps.logic.MiniAppOperation;
import com.spotapps.server.AWSAdapter;

import java.util.UUID;


public class CreateNewMiniAppActivity extends FragmentActivity implements EditMiniAppOperationFragmentListener {

    //Spot spot;
    private SaveMiniAppTask aSyncTask;

    // when the create mini app screen is created we want to update the
    // spinner with the mini app types and create the dynamic mini app param editor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_mini_app);

        Bundle bundle = getIntent().getExtras();
        SpotBundle spotBundle = SpotBundleUtils.loadSpotsFromBundle(bundle);



        Spinner spinner = (Spinner) findViewById(R.id.MiniAppTypeSpinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mini_app_types_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String miniAppType = adapterView.getAdapter().getItem(position).toString();
                replaceMiniAppTypeParamEditor(miniAppType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void replaceMiniAppTypeParamEditor(String miniAppTypeText) {

        MiniAppTypes miniAppTypes = MiniAppTypes.lookupByText(miniAppTypeText);
        String miniAppType = miniAppTypes.toString();
        // create the fragment to use
        Fragment fragment = null;
        if (MiniAppTypes.DIAL.equals(miniAppTypes)){
            fragment = EditMiniAppOperationParamsPhoneEditorFragment.newInstance(miniAppType, "TODO two");
        } else if (MiniAppTypes.OPEN_APP.equals(miniAppTypes)){
            fragment = EditMiniAppOperationParamsAppSpinnerFragment.newInstance(miniAppType, "TODO two");
        } else{
            fragment = EditMiniAppOperationParamsWebFragment.newInstance(miniAppType, "TODO two");
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.MiniAppParamFragmentContainer, fragment);

// Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_mini_app, menu);
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

    public void saveNewMiniApp(View view){
        Bundle bundle = getIntent().getExtras();
        SpotBundle spotBundle = SpotBundleUtils.loadSpotsFromBundle(bundle);
        Spot spot = spotBundle.getFirstSpot();

        EditText nameField =  (EditText)findViewById(R.id.editName);
        String name = nameField.getText().toString();
        Spinner typeSpinner =  (Spinner)findViewById(R.id.MiniAppTypeSpinner);
        String typeAsString =   typeSpinner.getSelectedItem().toString(); // TODO TALYAC selected item might not be the right string?
        String type = MiniAppTypes.lookupByText(typeAsString).toString();
        FrameLayout paramEditor = (FrameLayout) findViewById(R.id.MiniAppParamFragmentContainer);
        EditMiniAppOperationParamsFragment fragment = (EditMiniAppOperationParamsFragment) getSupportFragmentManager().findFragmentById(R.id.MiniAppParamFragmentContainer);
        String value = fragment.getValue();
        //EditMiniAppOperationParamsWebFragment valueField =  (EditMiniAppOperationParamsWebFragment)findViewById(R.id.MiniAppParamFragment);
        // String value = valueField.getText().toString();

        String id = UUID.randomUUID().toString();
        MiniApp miniApp = MiniAppFactory.createMiniApp(this, id, name, "web_link.png", type, value, spot, 10, 0);
        if (isValidMiniApp(miniApp)) {


            String text = "Saving mini app " + name + " for spot " + spot.getName();
            Toast.makeText(CreateNewMiniAppActivity.this, text, Toast.LENGTH_LONG).show();
            //Intents start another activity
            // TODO TALYAC start new service that creates new spot?
            // they all extend content and they are all part of the same context (like springmanager)
//        Intent intent = new Intent(this, CreateNewMiniAppActivity.class);
//        EditText et = (EditText) findViewById(R.id.edit_miniapp_name);
//        String msg = et.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, msg);
//        startActivity(intent);

            // according to type determine size of spot

            if (aSyncTask == null) { // TODO how do I rerun this and save only once if clicked twice.
                aSyncTask = new SaveMiniAppTask(this);
                aSyncTask.execute(miniApp);
            }
        } else {
            String text = "Failed to save mini app " + name + " for spot " + spot.getName();
            Toast.makeText(CreateNewMiniAppActivity.this, text, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidMiniApp(MiniApp miniApp) {
        MiniAppOperation op = new MiniAppOperation(miniApp, this);
        return op.validateIntentByOperation();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO TALYAC
    }

    public class SaveMiniAppTask extends AsyncTask<MiniApp, Integer, Boolean> {

        public SaveMiniAppTask(Context context) {
            this.context = context;
        }

        private Context context;

        protected Boolean doInBackground(MiniApp... keys) {
        /*
            To ensure that a task is cancelled as quickly as possible,
            you should always check the return value of isCancelled() periodically from doInBackground(Object[]),
            if possible (inside a loop for instance.)
            if possible (inside a loop for instance.)
         */
            // TODO extract location data from key
            MiniApp key = keys[0];
            if (key == null) {
                throw new RuntimeException("AHHHHHHHH!!!!!");
            }
            AWSAdapter awsAdapter = new AWSAdapter();
            boolean result = awsAdapter.saveNewMiniApp(this.context, key);
            // publishProgress(50);
            // TODO TALYAC here we can call publishProgress()
            // TODO TALYAC finish here?
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", key);
            if (result) {
                setResult(RESULT_OK, returnIntent);
            } else {
                setResult(RESULT_CANCELED, returnIntent);
                String text = "Failed to save mini app " + key.getName();
                Toast.makeText(CreateNewMiniAppActivity.this, text, Toast.LENGTH_LONG).show();
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
