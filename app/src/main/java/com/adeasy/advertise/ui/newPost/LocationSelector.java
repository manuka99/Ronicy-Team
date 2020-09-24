package com.adeasy.advertise.ui.newPost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.ui.advertisement.LocationPicker;
import com.google.android.material.textfield.TextInputLayout;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class LocationSelector extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int LOCATION_PICKER = 5654;
    private static final String LOCATION_SELECTED = "location_selected";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    NewPostViewModel newPostViewModel;
    Button btnSelect;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    TextInputLayout locationSelectNewPost;

    private static final String TAG = "LocationSelector";

    public LocationSelector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment locationSelector.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationSelector newInstance(String param1, String param2) {
        LocationSelector fragment = new LocationSelector();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manuka_fragment_location_selector, container, false);

        btnSelect = view.findViewById(R.id.setLocationNewAd);
        btnSelect.setOnClickListener(this);
        newPostViewModel = ViewModelProviders.of(getActivity()).get(NewPostViewModel.class);
        linearLayout = view.findViewById(R.id.locationExtraLayout);
        progressBar = view.findViewById(R.id.locationSelectorProgress);
        locationSelectNewPost = view.findViewById(R.id.locationSelectNewPost);

        locationSelectNewPost.setOnClickListener(this);
        locationSelectNewPost.getEditText().setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSelect)
            onUpdateLocation();
        else if (view == locationSelectNewPost || view == locationSelectNewPost.getEditText())
            startActivityForResult(new Intent(getActivity(), LocationPicker.class), LOCATION_PICKER);
    }

    private void onLocationSeleted() {
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LOCATION_PICKER && data != null) {
            locationSelectNewPost.setError(null);
            if (data.getStringExtra(LOCATION_SELECTED) != null)
                locationSelectNewPost.getEditText().setText(data.getStringExtra(LOCATION_SELECTED));
            else
                Toast.makeText(getActivity(), "Location selected is invalid!", Toast.LENGTH_LONG).show();
        }
    }

    private void onUpdateLocation() {
        if (locationSelectNewPost.getEditText().getText().length() > 0) {
            onLocationSeleted();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    newPostViewModel.setLocationSelected(locationSelectNewPost.getEditText().getText().toString());
                }
            }, 1000);
        } else
            locationSelectNewPost.setError("Please select a valid location for your ad.");
    }

}