package com.adeasy.advertise.ui.newPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.Promotion.PromotionMain;
import com.adeasy.advertise.ui.athentication.LoginRegister;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class NewAd extends AppCompatActivity implements AdvertisementCallback {

    FrameLayout frameLayout, frameLayout2, frameLayout3, frameLayout4;
    Toolbar toolbar;
    AllCategoriesNewPost allCategoriesNewPost;
    LocationSelector locationSelector;
    AdvertisementDetails advertisementDetails;
    CategorySelected categorySelected;
    ContactDetails contactDetails;
    LoginRegister loginRegister;
    List<Integer> verifiedNumbers;
    AdvertisementManager advertisementManager;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    NewPostViewModel newPostViewModel;

    Category category;
    String location;

    Advertisement advertisement;

    CustomDialogs customDialogs;

    private static final String TAG = "NewAd";
    private static final int LOCATION_PICKER = 5654;

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
        loginRegister = new LoginRegister();

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        category = new Category();


        toolbar = findViewById(R.id.toolbar);

        newPostViewModel = ViewModelProviders.of(this).get(NewPostViewModel.class);

        advertisementManager = new AdvertisementManager(this);

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

        newPostViewModel.getContactDetailsValidation().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> numbers) {
                verifiedNumbers = numbers;
                onContactDetailsValidated();
            }
        });

        customDialogs = new CustomDialogs(this);

        startShowAllCategories();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null)
            showLoginRegisterFragment();
        else if (!new InternetValidation().validateInternet(getApplicationContext()))
            customDialogs.showNoInternetDialog();
        else
            validateFragmentsOnStart();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment(2) != null || getCurrentFragment(3) != null)
            showExitAlert();
        else
            finish();
    }

    private void showLoginRegisterFragment() {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("frame", "post");
            loginRegister.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), loginRegister);
            fragmentTransaction.remove(locationSelector);
            fragmentTransaction.remove(categorySelected);
            fragmentTransaction.remove(contactDetails);
            fragmentTransaction.remove(advertisementDetails);
            fragmentTransaction.remove(allCategoriesNewPost);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startShowAllCategories() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", null);

        allCategoriesNewPost.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), allCategoriesNewPost);

        fragmentTransaction.remove(locationSelector);
        fragmentTransaction.remove(categorySelected);
        fragmentTransaction.remove(contactDetails);
        fragmentTransaction.remove(advertisementDetails);

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

        fragmentTransaction.remove(contactDetails);
        fragmentTransaction.remove(advertisementDetails);

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
       new AlertDialog.Builder(this)

                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

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

    private void onContactDetailsValidated() {
        newPostViewModel.setAdDetailsValidation(true);
        newPostViewModel.getAdvertisement().observe(this, new Observer<Advertisement>() {
            @Override
            public void onChanged(Advertisement ad) {
                advertisement = ad;
                postAd();
            }
        });
    }

    private void postAd() {
        advertisement.setUserID(firebaseAuth.getCurrentUser().getUid());
        advertisement.setNumbers(verifiedNumbers);
        advertisement.setLocation(location);
        advertisement.setCategoryID(category.getId());
        advertisementManager.uploadImageMultiple(advertisement, null, this);
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {
        if (result)
            Toast.makeText(this, "Please Wait..", Toast.LENGTH_SHORT).show();

        else {
            progressDialog.setTitle("Publishing your advertisement...");
            progressDialog.setMessage("Your advertisement will be live after we approve it.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    @Override
    public void onCompleteInsertAd(Task<Void> task) {
        if (task != null && task.isSuccessful()) {
            progressDialog.dismiss();
            Intent intent = new Intent(NewAd.this, PromotionMain.class);
            intent.putExtra(PromotionMain.ADVERTISEMENT_SUBMITTED, true);
            intent.putExtra(PromotionMain.ADVERTISEMENT_ID, advertisement.getId());
            startActivity(intent);
            finish();
        } else if (task != null) {
            progressDialog.dismiss();
            Toast.makeText(this, "error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == LOCATION_PICKER && data != null)
                locationSelector.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFragmentsOnStart() {
        try {
            if (category.getId() == null)
                startShowAllCategories();
            else if (location == null)
                startSelectLocation();
            else
                startShowAdAndContactDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }
}