package com.adeasy.advertise.ui.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.RecycleAdapterForSearchAds;
import com.adeasy.advertise.callback.AdvertismentSearchCallback;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.search_manager.AdvertismentSearchManager;

import java.util.ArrayList;
import java.util.List;

public class HomeAdSearch extends AppCompatActivity implements TextWatcher, AdvertismentSearchCallback {

    Toolbar toolbar;
    EditText search_keyword;
    AdvertismentSearchManager advertismentSearchManager;
    RecyclerView recyclerView;
    RecycleAdapterForSearchAds recycleAdapterForSearchAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_home_ad_search);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.myaddsRecycle);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24_search_key);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_keyword = toolbar.findViewById(R.id.search_keyword);

        search_keyword.addTextChangedListener(this);

        advertismentSearchManager = new AdvertismentSearchManager(this);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getApplication(), search_keyword.getText().toString(), Toast.LENGTH_LONG).show();
                    advertismentSearchManager.searchAdsHome(search_keyword.getText().toString());
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().length() > 0)
            advertismentSearchManager.searchAdsHome(editable.toString());
        else {
            recycleAdapterForSearchAds = new RecycleAdapterForSearchAds(new ArrayList<Advertisement>());
            recyclerView.setAdapter(recycleAdapterForSearchAds);
            recycleAdapterForSearchAds.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchComplete(List<String> ids, List<Advertisement> ads) {
        recycleAdapterForSearchAds = new RecycleAdapterForSearchAds(ads);
        recyclerView.setAdapter(recycleAdapterForSearchAds);
        recycleAdapterForSearchAds.notifyDataSetChanged();
    }

}