package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.fragment.Account;
import com.adeasy.advertise.fragment.Chat;
import com.adeasy.advertise.fragment.Home;
import com.adeasy.advertise.fragment.NewPost;
import com.adeasy.advertise.fragment.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.navBottm);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navHome);

    }

    Home home = new Home();
    Search search = new Search();
    NewPost newPost = new NewPost();
    Chat chat = new Chat();
    Account account = new Account();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "seleected");

        switch (menuItem.getItemId()){

            case R.id.navHome:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, home).commit();
                return true;

            case R.id.navSearch:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, search).commit();
                return true;

            case R.id.navAddPost:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, newPost).commit();
                return true;

            case R.id.navChat:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, chat).commit();
                return true;

            case R.id.navAccount:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.navContainer, account).commit();
                return true;

        }

        return false;
    }

}