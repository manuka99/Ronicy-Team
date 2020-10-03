package com.adeasy.advertise.ui.editAd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.editAd.SelectedCategory;
import com.adeasy.advertise.ui.editAd.ContactDetails;
import com.adeasy.advertise.ui.editAd.LocationSelector;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class EditAd extends AppCompatActivity implements AdvertisementCallback, CategoryCallback {

    FrameLayout frameLayout, frameLayout2, frameLayout3, frameLayout4;
    Toolbar toolbar;
    LocationSelector locationSelector;
    AdDetails adDetails;
    SelectedCategory categorySelected;
    ContactDetails contactDetails;
    List<Integer> verifiedNumbers;
    List<String> firebaseDeletedImages;
    AdvertisementManager advertisementManager;
    CategoryManager categoryManager;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    NewPostViewModel newPostViewModel;

    Category category;
    String adID, adCID;

    Advertisement advertisement;
    CustomDialogs customDialogs;

    private static final String TAG = "EditAd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_edit_ad);

        frameLayout = findViewById(R.id.editPostFragmentCont);
        frameLayout2 = findViewById(R.id.editPostFragmentCont2);
        frameLayout3 = findViewById(R.id.editPostFragmentCont3);
        frameLayout4 = findViewById(R.id.editPostFragmentCont4);

        locationSelector = new LocationSelector();
        adDetails = new AdDetails();
        categorySelected = new SelectedCategory();
        contactDetails = new ContactDetails();

        progressDialog = new ProgressDialog(this);
        customDialogs = new CustomDialogs(this);

        firebaseAuth = FirebaseAuth.getInstance();

        category = new Category();

        toolbar = findViewById(R.id.toolbar);

        newPostViewModel = ViewModelProviders.of(this).get(NewPostViewModel.class);

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit ad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adID = getIntent().getStringExtra("adID");
        adCID = getIntent().getStringExtra("adCID");

        advertisementManager.getAddbyID(adID);
        categoryManager.getCategorybyID(adCID);

        firebaseDeletedImages = new ArrayList<>();

        newPostViewModel.getContactDetailsValidation().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> numbers) {
                verifiedNumbers = numbers;
                onContactDetailsValidated();
            }
        });

        newPostViewModel.getDeletedFirebaseUriImages().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                firebaseDeletedImages = strings;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!new InternetValidation().validateInternet(getApplicationContext()))
            customDialogs.showNoInternetDialog();
    }

    @Override
    public void onBackPressed() {
        showExitAlert();
    }

    private void showSelectedCategoryFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        categorySelected.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), categorySelected);
        fragmentTransaction.commit();
    }

    private void showLocationFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("location", advertisement.getLocation());
        locationSelector.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout2.getId(), locationSelector);
        fragmentTransaction.commit();
    }

    private void showAdDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("advertisement", advertisement);
        adDetails.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout3.getId(), adDetails);
        fragmentTransaction.commit();
    }

    private void showContactDetailsFragments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("advertisement", advertisement);
        contactDetails.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout4.getId(), contactDetails);
        fragmentTransaction.commit();
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
        advertisement.setNumbers(verifiedNumbers);
        advertisementManager.uploadImageMultiple(advertisement, firebaseDeletedImages, this);
        Log.i(TAG, "updateing start");
    }

    public void showExitAlert() {
        new AlertDialog.Builder(this)

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
        if(task != null && task.isSuccessful()){
            progressDialog.dismiss();
            Toast.makeText(EditAd.this, "Success: Your advertisement was submited", Toast.LENGTH_LONG).show();
            finish();
        }else if(task != null){
            progressDialog.dismiss();
            Toast.makeText(this, "error: Your advertisement was not submited", Toast.LENGTH_LONG).show();
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
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
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                advertisement = new Advertisement();
                advertisement = document.toObject(Advertisement.class);
                showAdDetailsFragment();
                showContactDetailsFragments();
                showLocationFragment();
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                category = new Category();
                category = document.toObject(Category.class);
                showSelectedCategoryFragment();
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }

}