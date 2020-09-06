package com.adeasy.advertise.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.navHome);
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
        if (selectedMenueID != menuItem.getItemId()) {

            selectedMenueID = menuItem.getItemId();

            switch (menuItem.getItemId()) {

                case R.id.navHome:
                    changeToolbarHome();
                    getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, home).commit();
                    return true;

                case R.id.navSearch:
                    changeToolbarDefault();
                    getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, search).commit();
                    return true;

                case R.id.navAddPost:
                    changeToolbarDefault();
                    startActivity(new Intent(this, NewAd.class));
                    return true;

                case R.id.navChat:
                    changeToolbarDefault();
                    getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, chat).commit();
                    return true;

                case R.id.navAccount:
                    changeToolbarDefault();
                    getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, account).commit();
                    return true;
            }
        }

        return false;
    }

    public void changeToolbarHome() {
        toolbar.removeAllViews();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View home_logo = getLayoutInflater().inflate(R.layout.toolbar_home, null);
        toolbar.addView(home_logo);
    }

    public void changeToolbarDefault() {
        toolbar.removeAllViews();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

}