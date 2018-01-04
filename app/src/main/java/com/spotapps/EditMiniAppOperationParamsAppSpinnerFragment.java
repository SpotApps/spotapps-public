package com.spotapps;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.spotapps.beans.MiniAppTypes;
import com.spotapps.logic.EditMiniAppOperationFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditMiniAppOperationFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EditMiniAppOperationParamsAppSpinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMiniAppOperationParamsAppSpinnerFragment extends Fragment implements EditMiniAppOperationParamsFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MINI_APP_TYPE = "MINI_APP_TYPE";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String miniAppType;
    private String mParam2;

    private EditMiniAppOperationFragmentListener mListener;

    public EditMiniAppOperationParamsAppSpinnerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param miniAppType The type of mini app
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditMiniAppOperationParamsWebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMiniAppOperationParamsAppSpinnerFragment newInstance(String miniAppType, String param2) {
        EditMiniAppOperationParamsAppSpinnerFragment fragment = new EditMiniAppOperationParamsAppSpinnerFragment();
        Bundle args = new Bundle();
        args.putString(MINI_APP_TYPE, miniAppType);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            miniAppType = getArguments().getString(MINI_APP_TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_mini_app_operation_params_app, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.mini_app_type_app_spinner_fragment);

        PackageManager packageManager = getActivity().getPackageManager();
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities( mainIntent, 0);
        ArrayList<AppHolder> apps = new ArrayList<AppHolder>();
        apps.add(new AppHolder("",""));
        for (ResolveInfo ri : pkgAppsList) {
            String appName;
            String appPath;
            try {
                ApplicationInfo ai = packageManager.getApplicationInfo(ri.activityInfo.packageName, PackageManager.GET_META_DATA);
                appName = (String) packageManager.getApplicationLabel(ai);
                appPath = ai.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                appName = ri.activityInfo.loadLabel(packageManager).toString();
                appPath = appName;
            }

            apps.add(new AppHolder(appName, appPath));
        }

        // create the grid item mapping

        ArrayAdapter<AppHolder> adapter = new ArrayAdapter<AppHolder>(this.getActivity(), android.R.layout.simple_spinner_item, apps){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getView(0, convertView, parent);
            }
        };


        spinner.setAdapter(adapter);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String appPath = ((AppHolder)adapterView.getAdapter().getItem(i)).getAppPath();
                EditText paramEditor = (EditText) getView().findViewById(R.id.mini_app_type_app_txt_fragment);
                paramEditor.setText(appPath);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );
        //Spinner paramEditor = (Spinner) view.findViewById(R.id.mini_app_type_app_spinner_fragment);
        // paramEditor.setText(miniAppType); //explain what the user should do
        return view;
    }

    public void chooseApp(View view, String appName){
//        // Always use string resources for UI text.
//// This says something like "Share this photo with"
//        String title = "Enter";// getResources().getString(R.string.chooser_title);
//// Create intent to show chooser
//        Intent intent = new Intent(Intent.ACTION_CHOOSER);
//        IntentSender sender = ;
//        Intent chooser = Intent.createChooser(intent, title, sender);
//      //  String appName = chooser.getComponent().getPackageName();
//// Verify the intent will resolve to at least one activity
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivityForResult(chooser, 777777);
//        }

        // paramEditor.setText(miniAppType); //explain what the user should do
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void chooseApp(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditMiniAppOperationFragmentListener) {
            mListener = (EditMiniAppOperationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditMiniAppOperationFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getValue() {
        EditText paramEditor = (EditText) getView().findViewById(R.id.mini_app_type_app_txt_fragment);
        return paramEditor.getText().toString();
    }

    private class AppHolder {
        private final String appName;
        private final String appPath;

        public AppHolder(String appName, String appPath) {
            this.appName = appName;
            this.appPath = appPath;
        }

        public String getAppPath() {
            return appPath;
        }

        public String getAppName() {
            return appName;
        }

        @Override
        public String toString() {
            return getAppName();
        }
    }
}
