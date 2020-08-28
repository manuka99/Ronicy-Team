package com.adeasy.advertise.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Home home;
    Search search;
    Chat chat;
    Account account;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "seleected");

        switch (menuItem.getItemId()){

            case R.id.navHome:
                changeToolbarHome();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, home).commit();
                return true;

            case R.id.navSearch:
                changeToolbarDefault();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, search).commit();
                return true;

            case R.id.navAddPost:
                changeToolbarDefault();
                startActivity(new Intent(this, NewAd.class));
                return true;

            case R.id.navChat:
                changeToolbarDefault();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, chat).commit();
                return true;

            case R.id.navAccount:
                changeToolbarDefault();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, account).commit();
                return true;

        }

        return false;
    }

    public void changeToolbarHome(){
        toolbar.removeAllViews();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View home_logo = getLayoutInflater().inflate(R.layout.toolbar_home, null);
        toolbar.addView(home_logo);
    }

    public void changeToolbarDefault(){
        toolbar.removeAllViews();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

}