package com.adeasy.advertise.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Home home;
    Search search;
    Chat chat;
    Account account;
    int selectedMenueID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        showExitDialog();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)

                .setMessage("Are you sure you exit?")

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
                changeToolbarHome();
                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, home).commit();
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
    }

    public void changeToolbarDefault() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.removeAllViews();
        //getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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

}