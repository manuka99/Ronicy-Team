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
import java.util.List;

public class LocationPicker extends AppCompatActivity {

    TextView all_locations;
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
        recyclerView = findViewById(R.id.location_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        all_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(LOCATION_SELECTED, (String) null);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        recycleAdapterForLocationPicker = new RecycleAdapterForLocationPicker(addLocations(), this);

        recyclerView.setAdapter(recycleAdapterForLocationPicker);
    }

    private List<String> addLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        locations.add("Colombo");
        locations.add("Galle");
        locations.add("Mathara");
        locations.add("Jafna");
        return locations;
    }
}