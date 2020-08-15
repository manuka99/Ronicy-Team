package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.adeasy.advertise.activity.athentication.login;
import com.adeasy.advertise.activity.athentication.register;
import com.adeasy.advertise.firebase.AdvertisementFirebase;
import com.adeasy.advertise.firebase.AdvertisementFirebaseImpl;
import com.adeasy.advertise.firebase.CategoryFirebase;
import com.adeasy.advertise.firebase.CategoryFirebaseImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class Advertisement extends AppCompatActivity {

    TextView AdTitle, AdCondition, AdDescription, AdPrice, adTime, adCatName;
    ImageView image;
    String adID, adCID;
    AdvertisementFirebase advertisementFirebase;
    CategoryFirebase categoryFirebase;
    private static final String TAG = "EditAdvertisement";
    private com.adeasy.advertise.model.Advertisement advertisement;
    private com.adeasy.advertise.model.Category category;
    final private int requestCodeImage = 1456;
    Button callText, chatText, adBuyNow;
    Uri imageURI;
    Context context;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        auth = FirebaseAuth.getInstance();
        context = this;

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

        advertisementFirebase = new AdvertisementFirebaseImpl();
        categoryFirebase = new CategoryFirebaseImpl();

        image = findViewById(R.id.adDetailsImage);

        adCatName = findViewById(R.id.adDetailsCategoryName);
        adTime = findViewById(R.id.adDetailsTime);
        AdTitle = findViewById(R.id.adDetailsTitle);
        AdCondition = findViewById(R.id.adDetailsCondition);
        AdDescription = findViewById(R.id.adDetailsDescription);
        AdPrice = findViewById(R.id.adDetailsPrice);

        chatText = findViewById(R.id.adChatNow);
        callText = findViewById(R.id.adCallNow);
        adBuyNow = findViewById(R.id.adBuyNow);

        adID = getIntent().getStringExtra("adID");
        adCID = getIntent().getStringExtra("adCID");


        advertisementFirebase.getAddbyID(adID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        advertisement = new com.adeasy.advertise.model.Advertisement();
                        advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);

                        adTime.setText(advertisement.getPreetyTime());
                        AdTitle.setText(advertisement.getTitle());
                        AdCondition.setText(advertisement.getCondition());
                        AdDescription.setText(advertisement.getDescription());
                        AdPrice.setText("Rs. " + advertisement.getPrice());
                        Picasso.get().load(advertisement.getImageUrl()).into(image);

                        if(advertisement.isBuynow())
                            adBuyNow.setVisibility(View.VISIBLE);

                        else
                            adBuyNow.setVisibility(View.GONE);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        categoryFirebase.getCatbyID(adCID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
        });

        chatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        callText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 212145425"));
                startActivity(intent);
            }
        });

        adBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBuyNow();
            }
        });

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

    private void initBuyNow(){

        FirebaseUser user =  auth.getCurrentUser();

        if(user != null){

            AlertDialog alertDialog = new AlertDialog.Builder(Advertisement.this)

                    .setIcon(android.R.drawable.ic_dialog_alert)

                    .setTitle("You must be log in to place orders")

                    .setMessage("If you already have a account you can log in else register")

                    .setPositiveButton("login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(Advertisement.this, login.class));

                        }
                    })

                    .setNegativeButton("Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Advertisement.this, register.class));
                        }
                    })

                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })

                    .show();


        }else {
            Intent intent = new Intent(Advertisement.this, PlaceOrder.class);
            intent.putExtra("aID", advertisement.getId());
            intent.putExtra("cID", category.getId());
            startActivity(intent);
        }

    }

}