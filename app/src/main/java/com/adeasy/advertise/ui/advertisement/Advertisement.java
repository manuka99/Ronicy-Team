package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.AdvertisementSliderAdapter;
import com.adeasy.advertise.ui.Order.BuyNow;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Advertisement extends AppCompatActivity implements AdvertisementCallback, CategoryCallback, View.OnClickListener {

    TextView AdTitle, AdCondition, AdDescription, AdPrice, adTime, adCatName;
    SliderView sliderView;
    AdvertisementSliderAdapter advertisementSliderAdapter;
    String adID, adCID;
    Button callText, chatText, adBuyNow;
    Uri imageURI;
    Context context;
    FirebaseAuth auth;
    AdvertisementManager advertisementManager;
    CategoryManager categoryManager;
    com.adeasy.advertise.model.Advertisement advertisement;
    com.adeasy.advertise.model.Category category;
    private static final int requestCodeImage = 1456;
    private static final String TAG = "EditAdvertisement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_advertisement);

        auth = FirebaseAuth.getInstance();
        context = this;

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sliderView = findViewById(R.id.imageSlider);

        adCatName = findViewById(R.id.adDetailsCategoryName);
        adTime = findViewById(R.id.adDetailsTime);
        AdTitle = findViewById(R.id.adDetailsTitle);
        AdCondition = findViewById(R.id.adDetailsCondition);
        AdDescription = findViewById(R.id.adDetailsDescription);
        AdPrice = findViewById(R.id.adDetailsPrice);

        chatText = findViewById(R.id.adChatNow);
        callText = findViewById(R.id.adCallNow);
        adBuyNow = findViewById(R.id.adBuyNow);
        adBuyNow.setOnClickListener(this);

        adID = getIntent().getStringExtra("adID");
        adCID = getIntent().getStringExtra("adCID");

        advertisementManager.getAddbyID(adID);
        categoryManager.getCategorybyID(adCID);
        advertisementSliderAdapter = new AdvertisementSliderAdapter();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(Advertisement.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_share) {
            Toast.makeText(Advertisement.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initBuyNow() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {

            AlertDialog alertDialog = new AlertDialog.Builder(Advertisement.this)

                    .setIcon(android.R.drawable.ic_dialog_alert)

                    .setTitle("You are not logged in")

                    .setMessage("If you already have a account you can log in else register")

                    .setPositiveButton("login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //startActivity(new Intent(Advertisement.this, login.class));

                        }
                    })

                    .setNegativeButton("Proceed as a guest", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           placeBuyNow();
                        }
                    })

                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })

                    .show();


        } else
            placeBuyNow();


    }

    private void placeBuyNow() {
        Intent intent = new Intent(Advertisement.this, BuyNow.class);
        intent.putExtra("aID", advertisement.getId());
        intent.putExtra("cID", category.getId());
        startActivity(intent);
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onSuccessInsertAd() {

    }

    @Override
    public void onFailureInsertAd() {

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
                try {
                    advertisement = new com.adeasy.advertise.model.Advertisement();
                    advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);
                    adTime.setText(advertisement.getPreetyTime());
                    AdTitle.setText(advertisement.getTitle());
                    AdCondition.setText(advertisement.getCondition());
                    AdDescription.setText(advertisement.getDescription());
                    AdPrice.setText("Rs. " + advertisement.getPrice());
                    displayImageSlider();
                    if (advertisement.isBuynow())
                        adBuyNow.setVisibility(View.VISIBLE);

                    else
                        adBuyNow.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    @Override
    public void onClick(View view) {

        if (view == chatText) {


        } else if (view == callText) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:212145425"));
            startActivity(intent);
        } else if (view == adBuyNow)
            initBuyNow();

    }

    private void displayImageSlider(){
        advertisementSliderAdapter.renewItems(advertisement.getImageUrls());
        sliderView.setSliderAdapter(advertisementSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
        categoryManager.destroy();
    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                category = new com.adeasy.advertise.model.Category();
                category = document.toObject(com.adeasy.advertise.model.Category.class);

                adCatName.setText(category.getName());

            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

}