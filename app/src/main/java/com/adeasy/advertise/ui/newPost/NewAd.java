package com.adeasy.advertise.ui.newPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

    FrameLayout frameLayout, frameLayout2, frameLayout3;
    Toolbar toolbar;
    AllCategoriesNewPost allCategoriesNewPost;
    LocationSelector locationSelector;
    AdvertisementDetails advertisementDetails;
    CategorySelected categorySelected;

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

        allCategoriesNewPost = new AllCategoriesNewPost();
        locationSelector = new LocationSelector();
        advertisementDetails = new AdvertisementDetails();
        categorySelected = new CategorySelected();

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
                startShowAdDetails();
            }
        });

        startShowAllCategories();

    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof LocationSelector) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(getCurrentFragment());
            ft.commit();
            super.onBackPressed();
        }

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
        fragmentTransaction.addToBackStack(AllCategoriesNewPost.class.getName());
        fragmentTransaction.replace(frameLayout2.getId(), locationSelector);
        fragmentTransaction.replace(frameLayout.getId(), categorySelected);
        fragmentTransaction.commit();
    }

    private void startShowAdDetails() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        bundle.putString("location", location);
        allCategoriesNewPost.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(AllCategoriesNewPost.class.getName());
        fragmentTransaction.replace(frameLayout.getId(), allCategoriesNewPost);
        fragmentTransaction.add(frameLayout.getId(), advertisementDetails);
        fragmentTransaction.commit();
    }

    private Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(frameLayout.getId());
        return currentFragment;
    }

}