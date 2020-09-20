package com.adeasy.advertise.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String SEARCH_KEY = "search_key";

    private static final int SEARCH_BAR_RESULT = 133;

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Home home;
    Search search;
    Chat chat;
    Account account;
    int selectedMenueID = 0;
    String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            searchKey = getIntent().getStringExtra(SEARCH_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.navBottm);

        home = new Home();
        search = new Search();
        chat = new Chat();
        account = new Account();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.navHome);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeAllFragments();
        if (selectedMenueID != 0)
            bottomNavigationView.setSelectedItemId(selectedMenueID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//
//        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//
//        if (taskList.get(0).numActivities == 1 &&
//                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
//            Log.i(TAG, "This is last activity in the stack");
//            showExitDialog();
//        } else

        if (searchKey != null) {
            searchKey = null;
            bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)

                .setMessage("Are you sure you want to exit?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "seleected");
        initializeAllFragments();
        switch (menuItem.getItemId()) {
            case R.id.navHome:
                selectedMenueID = menuItem.getItemId();
                handleHomeFragment();
                return true;

            case R.id.navSearch:
                selectedMenueID = menuItem.getItemId();
                changeToolbarSearch();
                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, search).commit();
                return true;

            case R.id.navAddPost:
                changeToolbarDefault();
                startActivity(new Intent(this, NewAd.class));
                return true;

            case R.id.navChat:
                selectedMenueID = menuItem.getItemId();
                changeToolbarDefault();
                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, chat).commit();
                return true;

            case R.id.navAccount:
                selectedMenueID = menuItem.getItemId();
                changeToolbarDefault();
                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, account).commit();
                return true;
        }

        return false;
    }

    public void changeToolbarHome() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.removeAllViews();
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        View home_logo = getLayoutInflater().inflate(R.layout.toolbar_home, null);
        getSupportActionBar().setCustomView(home_logo);

        getSupportActionBar().setDisplayShowCustomEnabled(true); // enable overriding the default toolbar_home layout
        //getSupportActionBar().setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void changeToolbarDefault() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.removeAllViews();
        //getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setSubtitle("");
    }

    public void changeToolbarSearch() {
        toolbar.setVisibility(View.GONE);


        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //to fix force stop on loading fragments above
    private void initializeAllFragments() {
        home = new Home();
        search = new Search();
        chat = new Chat();
        account = new Account();
    }

    private void handleHomeFragment() {
        if (searchKey != null) {
            changeToolbarDefault();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_KEY, searchKey);
            home.setArguments(bundle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            changeToolbarHome();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, home).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_BAR_RESULT && resultCode == RESULT_OK && data != null) {
            searchKey = data.getStringExtra(SEARCH_KEY);
            bottomNavigationView.setSelectedItemId(R.id.navHome);
        }
    }

    //    @Override
//    public void onBackPressed() {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        int seletedItemId = bottomNavigationView.getSelectedItemId();
//        if (R.id.home != seletedItemId) {
//            setHomeItem(MainActivity.this);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    public static void setHomeItem(Activity activity) {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                activity.findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.home);
//    }

}