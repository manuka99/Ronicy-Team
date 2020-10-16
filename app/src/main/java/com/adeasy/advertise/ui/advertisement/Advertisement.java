package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.AdvertisementSliderAdapter;
import com.adeasy.advertise.adapter.RecyclerAdapterSimillarAds;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.helper.ViewHolderAdds;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.PromotionManager;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Favourite;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.ui.Order.BuyNow;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.ui.Promotion.PromotionMain;
import com.adeasy.advertise.ui.administration.advertisement.MoreActionsOnAd;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.ui.favaourite.AddToFavourite;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Advertisement extends AppCompatActivity implements AdvertisementCallback, CategoryCallback, PromotionCallback, View.OnClickListener {

    TextView AdTitle, AdCondition, AdDescription, AdPrice, adTime, adCatName, location;
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
    private static final int LIMIT_SIMILLAR_ADS = 8;

    List<String> bundle_ad_ids;

    ProgressBar progressBar, progressBarSimilarAds;
    LinearLayout adLayout;
    Button startPromotion;

    RecyclerView similarAds;

    CustomDialogs customDialogs;

    AdView adView1, adView2, adView4;
    PromotionManager promotionManager;
    List<Object> objectsForSimilarAds = new ArrayList<>();

    RecyclerAdapterSimillarAds recyclerAdapterSimillarAds;

    boolean isAddedToFavourite = false;
    Favourite favourite;

    public static final String ADVERTISEMENTID = "adID";
    public static final String CATID = "adCID";
    private static final String TAG = "EditAdvertisement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_advertisement);

        auth = FirebaseAuth.getInstance();
        context = this;

        advertisementManager = new AdvertisementManager(this);
        promotionManager = new PromotionManager(this);
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

        customDialogs = new CustomDialogs(this);

        progressBar = findViewById(R.id.progressBar);
        progressBarSimilarAds = findViewById(R.id.progressBarSimilarAds);
        adLayout = findViewById(R.id.adLayout);
        startPromotion = findViewById(R.id.startPromotion);
        adLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        similarAds = findViewById(R.id.similarAds);
        similarAds.setNestedScrollingEnabled(false);
        similarAds.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        adCatName = findViewById(R.id.adDetailsCategoryName);
        location = findViewById(R.id.location);
        adTime = findViewById(R.id.adDetailsTime);
        AdTitle = findViewById(R.id.adDetailsTitle);
        AdCondition = findViewById(R.id.adDetailsCondition);
        AdDescription = findViewById(R.id.adDetailsDescription);
        AdPrice = findViewById(R.id.adDetailsPrice);

        chatText = findViewById(R.id.adChatNow);
        callText = findViewById(R.id.adCallNow);
        adBuyNow = findViewById(R.id.adBuyNow);
        adBuyNow.setOnClickListener(this);
        startPromotion.setOnClickListener(this);

        if (getIntent().hasExtra(ADVERTISEMENTID))
            adID = getIntent().getStringExtra("adID");
        if (getIntent().hasExtra(CATID))
            adCID = getIntent().getStringExtra("adCID");

        if (!getIntent().hasExtra(ADVERTISEMENTID) && !getIntent().hasExtra(CATID))
            finish();

        advertisementManager.getAddbyID(adID);
        categoryManager.getCategorybyID(adCID);
        advertisementSliderAdapter = new AdvertisementSliderAdapter();

        //display ads

        adView1 = findViewById(R.id.adView1);
        adView2 = findViewById(R.id.adView2);
        adView4 = findViewById(R.id.adView4);
        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
        adView4.loadAd(new AdRequest.Builder().build());

        promotionManager.getAdIDsByPromotionType(Promotion.BUNDLE_AD);

        recyclerAdapterSimillarAds = new RecyclerAdapterSimillarAds(context);
        similarAds.setAdapter(recyclerAdapterSimillarAds);
        loadFavourite();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!new InternetValidation().validateInternet(getApplicationContext()))
            customDialogs.showNoInternetDialog();

        loadFavourite();

        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, adError.getMessage() + "Code : " + adError.getCode(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favourite, menu);

        if (isAddedToFavourite)
            menu.findItem(R.id.action_favorite).setVisible(false);
        else
            menu.findItem(R.id.action_favorite_added).setVisible(false);

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
            addToFavourite();
            return true;
        } else if (id == R.id.action_favorite_added) {
            removeFromFavourite();
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

            new AlertDialog.Builder(Advertisement.this)

                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

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
    public void onCompleteInsertAd(Task<Void> task) {

    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                try {
                    advertisement = new com.adeasy.advertise.model.Advertisement();
                    advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);
                    adTime.setText(advertisement.getPreetyTime());
                    AdTitle.setText(advertisement.getTitle());
                    AdCondition.setText(advertisement.getCondition());
                    AdDescription.setText(advertisement.getDescription());
                    AdPrice.setText(advertisement.getPreetyCurrency());
                    location.setText(advertisement.getLocation());
                    displayImageSlider();
                    if (advertisement.isBuynow())
                        adBuyNow.setVisibility(View.VISIBLE);

                    else
                        adBuyNow.setVisibility(View.GONE);

                    adLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No such document");
            }
        } else if (task != null) {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void onClick(View view) {

        if (view == chatText) {


        } else if (view == callText)
            callAdCustomer();
        else if (view == adBuyNow)
            initBuyNow();
        else if (view == startPromotion)
            startPromoteAd();

    }

    private void callAdCustomer() {
        ArrayAdapter<Integer> phone_numbers = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        phone_numbers.addAll(advertisement.getNumbers());

        new AlertDialog.Builder(this).setTitle(getString(R.string.phoneNumbers)).setAdapter(phone_numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + advertisement.getNumbers().get(i).toString())));
            }
        }).show();
    }

    private void displayImageSlider() {
        advertisementSliderAdapter.renewItems(advertisement.getImageUrls());
        sliderView.setSliderAdapter(advertisementSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
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
        if (task != null && task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                category = new com.adeasy.advertise.model.Category();
                category = document.toObject(com.adeasy.advertise.model.Category.class);

                adCatName.setText(category.getName());

            } else {
                Log.d(TAG, "No such document");
            }
        } else if (task != null) {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    private void startPromoteAd() {
        if (advertisement != null) {
            Intent intent = new Intent(getApplicationContext(), PromotionMain.class);
            intent.putExtra(ADVERTISEMENTID, advertisement.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onCompleteSavePromotion(Task<Void> task) {

    }

    @Override
    public void onGetPromotionByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onPromotionsListIds(List<String> ids, Query promotionQuery) {
        bundle_ad_ids = ids;
        if (bundle_ad_ids != null && bundle_ad_ids.size() > 0)
            loadBundleAds();
        else
            loadRegularAds(LIMIT_SIMILLAR_ADS);
    }

    @Override
    public void onGetAppliedApprovedPromotionByADID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onGetPendingPromotionsByADID(Task<QuerySnapshot> task) {

    }

    private void loadBundleAds() {
        advertisementManager.homeSimilarAds(adCID, bundle_ad_ids, LIMIT_SIMILLAR_ADS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //check if there are bundleAds
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    objectsForSimilarAds = new ArrayList<>();
                    objectsForSimilarAds.addAll(task.getResult().toObjects(com.adeasy.advertise.model.BundleAd.class));
                    recyclerAdapterSimillarAds.setObjects(objectsForSimilarAds);

                    if (objectsForSimilarAds.size() < LIMIT_SIMILLAR_ADS) {
                        //load regularAds
                        loadRegularAds(LIMIT_SIMILLAR_ADS - objectsForSimilarAds.size());
                    } else
                        progressBarSimilarAds.setVisibility(View.GONE);
                } else
                    loadRegularAds(LIMIT_SIMILLAR_ADS);
            }
        });
    }

    private void loadRegularAds(final int count) {
        advertisementManager.homeSimilarAds(adCID, null, count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        objectsForSimilarAds = new ArrayList<>();
                        objectsForSimilarAds.addAll(task.getResult().toObjects(com.adeasy.advertise.model.Advertisement.class));
                        recyclerAdapterSimillarAds.setObjects(objectsForSimilarAds);
                    } else if (count == LIMIT_SIMILLAR_ADS) {
                        //no data for similar ads
                    }
                } else if (count == LIMIT_SIMILLAR_ADS) {
                    //no data for similar ads
                }
                progressBarSimilarAds.setVisibility(View.GONE);
            }
        });
    }

    private void loadFavourite() {
        FirebaseFirestore.getInstance().collection(Favourite.COLLECTION_NAME).whereEqualTo(Favourite.AD_ID, adID).whereEqualTo(Favourite.USER_ID, auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                isAddedToFavourite = false;
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    isAddedToFavourite = true;
                    favourite = task.getResult().toObjects(Favourite.class).get(0);
                }
                invalidateOptionsMenu();
            }
        });
    }

    private void addToFavourite() {
        if (auth.getCurrentUser() != null) {
            Intent fav_intent = new Intent(getApplicationContext(), AddToFavourite.class);
            fav_intent.putExtra(ADVERTISEMENTID, adID);
            fav_intent.putExtra(AddToFavourite.FAV_STATUS, true);
            startActivity(fav_intent);
        } else
            Toast.makeText(getApplicationContext(), "Please sign in to manage your favourites", Toast.LENGTH_LONG).show();
    }

    private void removeFromFavourite() {
        if (auth.getCurrentUser() != null) {
            Intent fav_intent = new Intent(getApplicationContext(), AddToFavourite.class);
            fav_intent.putExtra(ADVERTISEMENTID, adID);
            fav_intent.putExtra(AddToFavourite.FAV_STATUS, false);
            fav_intent.putExtra(AddToFavourite.FAVOURITE, favourite);
            startActivity(fav_intent);
        } else
            Toast.makeText(getApplicationContext(), "Please sign in to manage your favourites", Toast.LENGTH_LONG).show();
    }

}