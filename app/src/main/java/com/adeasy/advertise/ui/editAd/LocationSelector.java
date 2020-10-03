package com.adeasy.advertise.ui.editAd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adeasy.advertise.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class LocationSelector extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout extraLayout;
    String location;
    TextInputLayout locationSelectNewPost;
    ProgressBar locationSelectorProgress;

    public LocationSelector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationSelector.
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.manuka_fragment_location_selector, container, false);

        location = getArguments().getString("location");

        extraLayout = view.findViewById(R.id.locationExtraLayout);
        locationSelectNewPost = view.findViewById(R.id.locationSelectNewPost);
        locationSelectorProgress = view.findViewById(R.id.locationSelectorProgress);

        extraLayout.setVisibility(View.GONE);
        locationSelectNewPost.getEditText().setText(location);
        locationSelectNewPost.getEditText().setTextColor(getResources().getColor(R.color.colorWhiteLite));

        locationSelectorProgress.setVisibility(View.GONE);

        return view;
    }
}