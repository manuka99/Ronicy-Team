package com.adeasy.advertise.ui.newPost;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.Order.OrderPhoneVerify;
import com.adeasy.advertise.ui.Order.Step2;

public class NewAd extends AppCompatActivity {

    FrameLayout frameLayout, frameLayout2, frameLayout3, frameLayout4;
    Toolbar toolbar;
    AllCategoriesNewPost allCategoriesNewPost;
    LocationSelector locationSelector;
    AdvertisementDetails advertisementDetails;
    CategorySelected categorySelected;
    ContactDetails contactDetails;

    NewPostViewModel newPostViewModel;

    Category category;
    String location;

    private static final String TAG = "NewAd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_new_ad_categories);
        frameLayout = findViewById(R.id.newPostFragmentCont);
        frameLayout2 = findViewById(R.id.newPostFragmentCont2);
        frameLayout3 = findViewById(R.id.newPostFragmentCont3);
        frameLayout4 = findViewById(R.id.newPostFragmentCont4);

        allCategoriesNewPost = new AllCategoriesNewPost();
        locationSelector = new LocationSelector();
        advertisementDetails = new AdvertisementDetails();
        categorySelected = new CategorySelected();
        contactDetails = new ContactDetails();

        category = new Category();


        toolbar = findViewById(R.id.toolbar);

        newPostViewModel = ViewModelProviders.of(this).get(NewPostViewModel.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        newPostViewModel.getCategorySelected().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category categorySelected) {
                category = categorySelected;
                startSelectLocation();
            }
        });

        newPostViewModel.getLocationSelected().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String locationSelected) {
                location = locationSelected;
                startShowAdAndContactDetails();
            }
        });

        newPostViewModel.getShowAllCategories().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        startShowAllCategories();

    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment(2) != null || getCurrentFragment(3) != null)
            showExitAlert();
        else
            finish();
    }

    private void startShowAllCategories() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", null);

        allCategoriesNewPost.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), allCategoriesNewPost);
        fragmentTransaction.commit();
    }

    private void startSelectLocation() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        categorySelected.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout2.getId(), locationSelector);
        fragmentTransaction.replace(frameLayout.getId(), categorySelected);
        fragmentTransaction.commit();
    }

    private void startShowAdAndContactDetails() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout3.getId(), advertisementDetails);
        fragmentTransaction.replace(frameLayout4.getId(), contactDetails);
        fragmentTransaction.commit();
    }

    private Fragment getCurrentFragment(int caseValue) {
        Fragment currentFragment;
        switch (caseValue) {
            case 1:
                currentFragment = getSupportFragmentManager().findFragmentById(frameLayout.getId());
                return currentFragment;

            case 2:
                currentFragment = getSupportFragmentManager().findFragmentById(frameLayout2.getId());
                return currentFragment;

            case 3:
                currentFragment = getSupportFragmentManager().findFragmentById(frameLayout3.getId());
                return currentFragment;
        }
        return null;
    }

    public void showExitAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("Are you sure you want to leave this page?")

                .setMessage("Please note that any details you have filled in will not be saved.")

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

}