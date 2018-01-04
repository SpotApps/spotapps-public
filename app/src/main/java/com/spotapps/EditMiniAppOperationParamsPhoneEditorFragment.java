package com.spotapps;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.spotapps.logic.EditMiniAppOperationFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditMiniAppOperationFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EditMiniAppOperationParamsPhoneEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMiniAppOperationParamsPhoneEditorFragment extends Fragment implements EditMiniAppOperationParamsFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MINI_APP_TYPE = "MINI_APP_TYPE";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String miniAppType;
    private String mParam2;

    private EditMiniAppOperationFragmentListener mListener;

    public EditMiniAppOperationParamsPhoneEditorFragment() {
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
    public static EditMiniAppOperationParamsPhoneEditorFragment newInstance(String miniAppType, String param2) {
        EditMiniAppOperationParamsPhoneEditorFragment fragment = new EditMiniAppOperationParamsPhoneEditorFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_mini_app_operation_params_phone, container, false);
        EditText paramEditor = (EditText) view.findViewById(R.id.mini_app_type_phone_txt_fragment);

        //paramEditor.setText(miniAppType);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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
        EditText paramEditor = (EditText) getView().findViewById(R.id.mini_app_type_phone_txt_fragment);
        return paramEditor.getText().toString();
    }

}
