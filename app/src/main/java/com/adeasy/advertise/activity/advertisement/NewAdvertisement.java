package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.adeasy.advertise.R;
import com.adeasy.advertise.firebase.AdvertisementFirebase;
import com.adeasy.advertise.firebase.AdvertisementFirebaseImpl;
import com.adeasy.advertise.model.Advertisement;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class NewAdvertisement extends AppCompatActivity {

    Context context;

    String categoryID;
    EditText newAddTitle, newAdCondition, newAdDescription, newAdPrice;
    ImageView imageView;
    Button addImage, postAd;
    Uri uri;
    final static int requestCodeImage = 1252;

    AdvertisementFirebase advertisementFirebase = new AdvertisementFirebaseImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advertisement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post your ad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        context = this;
        categoryID = getIntent().getStringExtra("key");

        newAddTitle = findViewById(R.id.newAdTitle);
        newAdCondition = findViewById(R.id.newAdSubject);
        newAdDescription = findViewById(R.id.newAdDescription);
        newAdPrice = findViewById(R.id.newAdPrice);

        imageView = findViewById(R.id.adNewImage);

        addImage = findViewById(R.id.adNewImageBtn);
        postAd = findViewById(R.id.adNewPostBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });

        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = newAddTitle.getText().toString();
                String condition = newAdCondition.getText().toString();
                String description = newAdDescription.getText().toString();
                Double price = 0.00;

                try {
                    price = Double.parseDouble(newAdPrice.getText().toString());
                }catch (Exception e){
                    price = 0.00;
                }

                if (uri != null && !title.isEmpty() && !condition.isEmpty() && !description.isEmpty() && price > 0) {

                    Advertisement advertisement = new Advertisement();
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

                    advertisementFirebase.uploadImage(advertisement, data, context);
                }

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
}