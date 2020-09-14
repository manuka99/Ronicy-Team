package com.adeasy.advertise.ui.administration.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.AdminAdPageAdapter;
import com.adeasy.advertise.ui.administration.home.DashboardHome;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class AdvertisementMain extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    AdminAdPageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_admin_advertisement_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dashboard v6.1");
        getSupportActionBar().setSubtitle("Manage advertisements");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new AdminAdPageAdapter(getSupportFragmentManager(), 3);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_ad_tool_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.action_admin_ads_search:
                startSearchActivity();
                return true;

            case R.id.action_admin_ads_live:
                startActivityOnItemSelected("live");
                return true;

            case R.id.action_admin_ads_hidden:
                startActivityOnItemSelected("hidden");
                return true;

            case R.id.action_admin_ads_buynow:
                startActivityOnItemSelected("buynow");
                return true;

            case R.id.action_admin_ads_trash:
                startActivityOnItemSelected("trash");
                return true;

            default:
                return false;
        }
    }

    private void startActivityOnItemSelected(String filterType) {
        Intent intent = new Intent(this, SelectCategoryAndFilter.class);
        intent.putExtra("filterType", filterType);
        startActivity(intent);
    }

    private void startSearchActivity() {
        startActivity(new Intent(this, DashboardHome.class));
    }

}