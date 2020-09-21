package com.adeasy.advertise.ui.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.adeasy.advertise.ui.home.MainActivity;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class HomeAdSearch extends AppCompatActivity implements TextWatcher, AdvertismentSearchCallback {

    Toolbar toolbar;
    EditText search_keyword;
    AdvertismentSearchManager advertismentSearchManager;
    RecyclerView recyclerView;
    RecycleAdapterForSearchAds recycleAdapterForSearchAds;
    Context context;
    Boolean isHavingResult = false;

    private static final String TAG = "HomeAdSearch";
    private static final String SEARCH_KEY = "search_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_home_ad_search);

        context = this;

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.myaddsRecycle);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24_search_key);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleAdapterForSearchAds = new RecycleAdapterForSearchAds(context);
        recyclerView.setAdapter(recycleAdapterForSearchAds);

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
                return true;
            }
        });

        search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Toast.makeText(getApplication(), search_keyword.getText().toString(), Toast.LENGTH_LONG).show();
                    //advertismentSearchManager.searchAdsHome(search_keyword.getText().toString());
                    openSearchIntent();
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
        isHavingResult = false;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        isHavingResult = false;
        if (!search_keyword.getText().toString().trim().isEmpty())
            advertismentSearchManager.searchAdsHome(search_keyword.getText().toString().trim());
        else {
            recycleAdapterForSearchAds.setData(new ArrayList<Advertisement>());
            recycleAdapterForSearchAds.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchComplete(List<String> ids, List<Advertisement> ads) {
        if (ids.size() > 0)
            isHavingResult = true;
        if (!search_keyword.getText().toString().trim().isEmpty()) {
            recycleAdapterForSearchAds.setData(ads);
            recycleAdapterForSearchAds.notifyDataSetChanged();
        } else {
            recycleAdapterForSearchAds.setData(new ArrayList<Advertisement>());
            recycleAdapterForSearchAds.notifyDataSetChanged();
        }
    }

    private void openSearchIntent() {
        if (!search_keyword.getText().toString().trim().isEmpty() && isHavingResult) {
            Intent intentEnd = new Intent();
            intentEnd.putExtra(SEARCH_KEY, search_keyword.getText().toString().trim());
            setResult(RESULT_OK, intentEnd);
            finish();
        } else if (search_keyword.getText().toString().trim().isEmpty())
            Toast.makeText(context, "Search keyword cannot be empty", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplication(), "No matching results found", Toast.LENGTH_LONG).show();
    }

}