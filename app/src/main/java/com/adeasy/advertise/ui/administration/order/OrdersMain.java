package com.adeasy.advertise.ui.administration.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.adeasy.advertise.R;
import com.google.android.material.navigation.NavigationView;

public class OrdersMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Dashboard dashboard;
    RecentOrders recentOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_admin_activity_orders_main);

        toolbar = findViewById(R.id.toolbar);

        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawwelayout);
        frameLayout = findViewById(R.id.frameLayout);


        //navigationView.bringToFront();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_list_24);

        //for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.dashboardVersion));
        getSupportActionBar().setSubtitle("Manage advertisements");

        //toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        //fragments
        dashboard = new Dashboard();
        recentOrders = new RecentOrders();

        //show dashborad on home
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));
        navigationView.setCheckedItem(R.id.home);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), dashboard).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.recent:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), recentOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

}