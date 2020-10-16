package com.adeasy.advertise.ui.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.adeasy.advertise.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class FilterSearchResult extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Spinner sortBySpinner, typeOfPosterSpinner;
    ImageView checkbox_urgent;
    ArrayAdapter<CharSequence> adapter_sortBySpinner, adapter_typeOfPosterSpinner;
    List<String> sortByArray, typeOfPosterArray;
    Boolean isChecked = false;
    TextView apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_filter_search_result);

        toolbar = findViewById(R.id.toolbar);
        sortBySpinner = findViewById(R.id.sortBySpinner);
        typeOfPosterSpinner = findViewById(R.id.typeOfPosterSpinner);
        checkbox_urgent = findViewById(R.id.checkbox_urgent);
        apply = findViewById(R.id.apply);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Filter search result");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //spinners
        sortByArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ads_sort_by_array)));
        typeOfPosterArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.type_of_posters_array)));

        //set adapter
        adapter_sortBySpinner = ArrayAdapter.createFromResource(this,
                R.array.ads_sort_by_array, android.R.layout.simple_spinner_item);
        adapter_typeOfPosterSpinner = ArrayAdapter.createFromResource(this,
                R.array.type_of_posters_array, android.R.layout.simple_spinner_item);

        adapter_sortBySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_typeOfPosterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBySpinner.setAdapter(adapter_sortBySpinner);
        typeOfPosterSpinner.setAdapter(adapter_typeOfPosterSpinner);

        //values selected in spinners
        sortBySpinner.getSelectedItem().toString();
        typeOfPosterSpinner.getSelectedItem().toString();

        //listeners
        checkbox_urgent.setOnClickListener(this);
        apply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == checkbox_urgent) {
            if (!isChecked) {
                isChecked = true;
                checkbox_urgent.setBackgroundResource(R.drawable.ic_checkbox_checked);
            } else {
                isChecked = false;
                checkbox_urgent.setBackgroundResource(R.drawable.ic_checkbox_normal);
            }
        }
    }

}