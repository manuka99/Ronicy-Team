package com.adeasy.advertise.ui.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.RecycleAdapterForLocationPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Store;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class LocationPicker extends AppCompatActivity implements RecycleAdapterForLocationPicker.ContactActivityInterface {

    TextView all_locations, backToAllLocations, mainDistrict;
    Toolbar toolbar;
    private static final String LOCATION_SELECTED = "location_selected";
    RecycleAdapterForLocationPicker recycleAdapterForLocationPicker;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_location_picker);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pick a location");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        all_locations = findViewById(R.id.all_locations);

        backToAllLocations = findViewById(R.id.backToAllLocations);
        mainDistrict = findViewById(R.id.mainDistrict);
        backToAllLocations.setVisibility(View.GONE);
        mainDistrict.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.location_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        all_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(LOCATION_SELECTED, (String) null);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        backToAllLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recycleAdapterForLocationPicker != null) {
                    recycleAdapterForLocationPicker.upDateAllLocations();
                    backToAllLocations.setVisibility(View.GONE);
                    mainDistrict.setVisibility(View.GONE);
                }
            }
        });

        recycleAdapterForLocationPicker = new RecycleAdapterForLocationPicker(Arrays.asList(getResources().getStringArray(R.array.locations_main)), this, this);

        recyclerView.setAdapter(recycleAdapterForLocationPicker);
    }

    public void showBackToAllLocations() {
        backToAllLocations.setVisibility(View.VISIBLE);
    }

    public void hideBackToAllLocations() {
        backToAllLocations.setVisibility(View.VISIBLE);
    }


    @Override
    public void toggleBackToAllAds(Boolean b) {
        if (b)
            showBackToAllLocations();
        else {
            hideBackToAllLocations();
            mainDistrictSelected(null);
        }
    }

    @Override
    public void mainDistrictSelected(String district) {
        if (district != null) {
            mainDistrict.setText(district);
            mainDistrict.setVisibility(View.VISIBLE);
        } else
            mainDistrict.setVisibility(View.GONE);
    }

}