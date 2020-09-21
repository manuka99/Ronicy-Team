package com.adeasy.advertise.ui.administration.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.adeasy.advertise.R;
import com.google.android.material.navigation.NavigationView;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class OrdersMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Dashboard dashboard;
    PendingOrders recentOrders;
    PastOrders pastOrders;
    OrderSales orderSales;
    ProductStatisticsFragment productStatisticsFragment;

    private static final String FRAGMENT_SECTION_KEY = "pending_order_section";
    private static final String RECENT = "recent";
    private static final String ONLINE_PAYMENTS = "online";
    private static final String COD_DELIVERY = "cod";
    private static final String COMPLETED = "completed";
    private static final String CANCELLED = "cancelled";

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
        getSupportActionBar().setSubtitle("Manage orders");

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
        recentOrders = new PendingOrders();
        orderSales = new OrderSales();
        productStatisticsFragment = new ProductStatisticsFragment();

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
        //destroyCurrentFragment();
        getSupportFragmentManager().popBackStack();
        switch (itemID) {
            case R.id.home:
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), dashboard).commit();
                break;
            case R.id.recent:
                recentOrders = new PendingOrders();
                Bundle bundle = new Bundle();
                bundle.putString(FRAGMENT_SECTION_KEY, RECENT);
                recentOrders.setArguments(bundle);
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), recentOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.onlinePending:
                recentOrders = new PendingOrders();
                Bundle bundle2 = new Bundle();
                bundle2.putString(FRAGMENT_SECTION_KEY, ONLINE_PAYMENTS);
                recentOrders.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), recentOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.codPending:
                recentOrders = new PendingOrders();
                Bundle bundle3 = new Bundle();
                bundle3.putString(FRAGMENT_SECTION_KEY, COD_DELIVERY);
                recentOrders.setArguments(bundle3);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), recentOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.completed:
                pastOrders = new PastOrders();
                Bundle bundle4 = new Bundle();
                bundle4.putString(FRAGMENT_SECTION_KEY, COMPLETED);
                pastOrders.setArguments(bundle4);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), pastOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.cancelled:
                pastOrders = new PastOrders();
                Bundle bundle5 = new Bundle();
                bundle5.putString(FRAGMENT_SECTION_KEY, CANCELLED);
                pastOrders.setArguments(bundle5);
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), pastOrders).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.monthAnalysis:
                orderSales = new OrderSales();
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), orderSales).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.productAnalysis:
                productStatisticsFragment = new ProductStatisticsFragment();
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), productStatisticsFragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void destroyCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(frameLayout.getId());
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

}