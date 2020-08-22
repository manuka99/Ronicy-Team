package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.util.ImageQualityReducer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class EditAdvertisement extends AppCompatActivity implements AdvertisementCallback, View.OnClickListener {

    EditText editAdTitle, editAdCondition, editAdDescription, editAdPrice;
    Button choseImageBtn, updatePostBtn;
    ImageView image;
    String adID, adCID;
    AdvertisementManager advertisementManager;
    Advertisement advertisement;
    Uri imageURI;
    Context context;
    ProgressDialog progressDialog;
    private final int requestCodeImage = 1456;
    private static final String TAG = "EditAdvertisement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advertisement);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit your ad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        advertisementManager = new AdvertisementManager(this);

        image = findViewById(R.id.editAdImage);

        editAdTitle = findViewById(R.id.editAdTitle);
        editAdCondition = findViewById(R.id.editAdSubject);
        editAdDescription = findViewById(R.id.editAdDescription);
        editAdPrice = findViewById(R.id.editAdPrice);

        choseImageBtn = findViewById(R.id.editAdChangeImageBtn);
        updatePostBtn = findViewById(R.id.editAdUpdateBtn);

        adID = getIntent().getStringExtra("adID");
        adCID = getIntent().getStringExtra("adCID");

        progressDialog = new ProgressDialog(context);

        advertisementManager.getAddbyID(adID);

    }

    public void showAlert() {

        AlertDialog alertDialog = new AlertDialog.Builder(EditAdvertisement.this)

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

    public void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCodeImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeImage && resultCode == -1 && data != null && data.getData() != null) {
            imageURI = data.getData();
            image.setBackgroundColor(000000);
            image.setImageURI(imageURI);
        }

    }

    public void validateEditedAd(){

        if (editAdTitle.length() == 0)
            editAdTitle.setError("Please enter title of your ad!");

        else if (editAdCondition.length() < 10)
            editAdCondition.setError("Title of your ad is short!");

        else if (editAdDescription.length() == 0)
            editAdDescription.setError("Please write a description for your ad!");

        else if (editAdPrice.length() == 0)
            editAdPrice.setError("Please give a price for your ad!");

        else {

            AlertDialog alertDialog = new AlertDialog.Builder(EditAdvertisement.this)

                    .setIcon(android.R.drawable.ic_dialog_alert)

                    .setTitle("Are you sure you want to update your advertisement")

                    .setMessage("Note: your ad will be live once we approve it, approval may take upto 4 hours.")

                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            updateAd();

                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })

                    .show();


        }

    }

    private void updateAd() {

        advertisement.setTitle(editAdTitle.getText().toString());
        advertisement.setCondition(editAdCondition.getText().toString());
        advertisement.setDescription(editAdDescription.getText().toString());
        advertisement.setPrice(Double.parseDouble(editAdPrice.getText().toString()));
        advertisement.setPlacedDate(new Date());

        advertisement.setAvailability(true);
        advertisement.setApproved(false);

        if (imageURI != null) {
            image.setDrawingCacheEnabled(true);
            image.buildDrawingCache();
            advertisementManager.uploadImage(advertisement, ImageQualityReducer.reduceQuality(image.getDrawable()));
        } else
            advertisementManager.insertAdvertisement(advertisement);

    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

        if (task.isSuccessful()) {
            Uri downloadUri = task.getResult();

            if(advertisement.getImageUrl() != null)
                advertisementManager.deletePreviousImage(advertisement.getImageUrl());

            advertisement.setImageUrl(downloadUri.toString());
            advertisementManager.insertAdvertisement(advertisement);

        } else {
            // Handle failures
            // ...
        }
    }

    @Override
    public void onTaskFull(boolean result) {

        if(result)
            Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();

        else{
            progressDialog.setTitle("Publishing your advertisement...");
            progressDialog.setMessage("Your advertisement will be live after we approve it.");
            progressDialog.show();
        }

    }

    @Override
    public void onSuccessInsertAd() {
        progressDialog.dismiss();
        Toast.makeText(context, "Success: Your advertisement was submited", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailureInsertAd() {
        progressDialog.dismiss();
        Toast.makeText(context, "error: Your advertisement was not submited", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessDeleteAd() {

    }

    @Override
    public void onFailureDeleteAd() {

    }

    @Override
    public void onSuccessUpdatetAd() {

    }

    @Override
    public void onFailureUpdateAd() {

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

                editAdTitle.setText(advertisement.getTitle());
                editAdCondition.setText(advertisement.getCondition());
                editAdDescription.setText(advertisement.getDescription());
                editAdPrice.setText(String.valueOf(advertisement.getPrice()));
                Picasso.get().load(advertisement.getImageUrl()).into(image);
                image.setBackgroundColor(000000);
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }

    }

    @Override
    public void onClick(View view) {

        if(view == choseImageBtn)
            Filechooser();

        else if(view == image)
            Filechooser();

        else if(view == updatePostBtn)
            validateEditedAd();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }

}