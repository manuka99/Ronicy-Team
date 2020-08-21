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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class NewAdvertisement extends AppCompatActivity implements AdvertisementCallback, View.OnClickListener {

    Context context;
    String categoryID;
    EditText newAddTitle, newAdCondition, newAdDescription, newAdPrice;
    ImageView imageView;
    Button addImage, postAd;
    Uri uri;
    AdvertisementManager advertisementManager;
    ProgressDialog progressDialog;
    Advertisement advertisement;

    private final static int requestCodeImage = 1252;
    private static final String TAG = "NewAdvertisement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advertisement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post your ad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advertisementManager = new AdvertisementManager(this);

        context = this;
        categoryID = getIntent().getStringExtra("key");

        newAddTitle = findViewById(R.id.newAdTitle);
        newAdCondition = findViewById(R.id.newAdSubject);
        newAdDescription = findViewById(R.id.newAdDescription);
        newAdPrice = findViewById(R.id.newAdPrice);

        imageView = findViewById(R.id.adNewImage);

        addImage = findViewById(R.id.adNewImageBtn);
        postAd = findViewById(R.id.adNewPostBtn);

        progressDialog = new ProgressDialog(context);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

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
            uri = data.getData();
            imageView.setBackgroundColor(000000);
            imageView.setImageURI(uri);
        }

    }

    public void postNewAdd() {

        String title = newAddTitle.getText().toString();
        String condition = newAdCondition.getText().toString();
        String description = newAdDescription.getText().toString();
        Double price = 0.00;

        try {
            price = Double.parseDouble(newAdPrice.getText().toString());
        } catch (Exception e) {
            price = 0.00;
        }

        if (uri != null && !title.isEmpty() && !condition.isEmpty() && !description.isEmpty() && price > 0) {

            advertisement = new Advertisement();
            advertisement.setTitle(title);
            advertisement.setCondition(condition);
            advertisement.setDescription(description);
            advertisement.setPrice(price);
            advertisement.setCategoryID(categoryID);
            advertisement.setPlacedDate(new Date());

            advertisement.setApproved(true);
            advertisement.setAvailability(true);
            advertisement.setUserID("test123");

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            advertisementManager.uploadImage(advertisement, data);

        }
    }

    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(NewAdvertisement.this)

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

        if (task.isSuccessful()) {
            Uri downloadUri = task.getResult();

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
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }

    @Override
    public void onClick(View view) {

        if(view == imageView)
            Filechooser();

        else if(view == addImage)
            Filechooser();

        else if(view == postAd)
            postNewAdd();

    }
}