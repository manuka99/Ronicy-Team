package com.adeasy.advertise.ui.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.Order.OrderPhoneVerify;
import com.adeasy.advertise.ui.Order.Step2;

public class NewAdCategories extends AppCompatActivity {

    FrameLayout frameLayout;
    Toolbar toolbar;
    AllCategoriesNewPost allCategoriesNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_new_ad_categories);
        frameLayout = findViewById(R.id.newPostFragmentCont);
        allCategoriesNewPost = new AllCategoriesNewPost();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        startShowAllCategories();

    }

    @Override
    public void onBackPressed() {

        if (getCurrentFragment() instanceof OrderPhoneVerify || getCurrentFragment() instanceof Step2) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(getCurrentFragment());
            ft.commit();
            super.onBackPressed();
        } else
            super.onBackPressed();

    }

    private void startShowAllCategories() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), allCategoriesNewPost);
        fragmentTransaction.commit();
    }

    private Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(frameLayout.getId());
        return currentFragment;
    }

}