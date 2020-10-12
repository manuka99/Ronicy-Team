package com.adeasy.advertise.ui.Promotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.adeasy.advertise.util.Promotions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class Payment extends AppCompatActivity implements AdvertisementCallback, CategoryCallback, View.OnClickListener{

    Toolbar toolbar;
    Button continueBTN;
    Map<Integer, Integer> promos;
    String advertisementID;
    LinearLayout continueLayout, mainLayout;
    ProgressBar progressBar;
    AdvertisementManager advertisementManager;
    CategoryManager categoryManager;

    //ad layout
    ImageView adImage;
    TextView adTitle, adCategory, adPrice;
    Advertisement advertisement;

    //promotions layouts
    LinearLayout bundle_layout, daily_bump_layout, top_ad_layout, urgent_layout, spotlight_layout;

    //total
    TextView totalView;
    double totalSum;

    //invalid promos
    TextView invalid_promos;

    private static final String TAG = "Payment";

    private static final String PROMOS_ADDED = "promos_added";
    private static final String ADVERTISEMENT_ID = "adID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_promotion_activity_payment);

        //main layouts
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);
        continueLayout = findViewById(R.id.continueLayout);
        continueBTN = findViewById(R.id.continueBTN);
        invalid_promos = findViewById(R.id.invalid_promos);

        //hide all and load
        continueLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        invalid_promos.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        continueBTN.setBackgroundResource(R.drawable.button_round_grey);

        //ad layout
        adTitle = findViewById(R.id.title);
        adImage = findViewById(R.id.adImage);
        adCategory = findViewById(R.id.category);
        adPrice = findViewById(R.id.adPrice);

        //promo layouts
        bundle_layout = findViewById(R.id.bundle_layout);
        daily_bump_layout = findViewById(R.id.daily_bump_layout);
        top_ad_layout = findViewById(R.id.top_ad_layout);
        urgent_layout = findViewById(R.id.urgent_layout);
        spotlight_layout = findViewById(R.id.spotlight_layout);

        //total
        totalView = findViewById(R.id.total);

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);

        //checkIntent values
        if (getIntent().hasExtra(PROMOS_ADDED))
            promos = (Map<Integer, Integer>) getIntent().getSerializableExtra(PROMOS_ADDED);

        if (getIntent().hasExtra(ADVERTISEMENT_ID))
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);

        advertisementManager.getAddbyID(advertisementID);

        //toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //set listeners
        continueBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == continueBTN)
            checkout();
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
            advertisement = task.getResult().toObject(Advertisement.class);

            //get the category details
            categoryManager.getCategorybyID(advertisement.getCategoryID());

            //add layouts and views
            adTitle.setText(advertisement.getTitle());
            Picasso.get().load(advertisement.getImageUrls().get(0)).into(adImage);
            adPrice.setText(advertisement.getPreetyCurrency());

            //complete the promotions
            showPromotions();

            //show layout
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            continueLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful()) {
            Category category = task.getResult().toObject(Category.class);
            adCategory.setText(category.getName());
        }
    }

    private void showPromotions() {
        hideAllPromosLayouts();
        if (promos != null) {
            totalSum = 0;
            for (Integer promoType : promos.keySet()) {

                int daysSelected = promos.get(promoType);

                if (promoType == Promotion.BUNDLE_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.BUNDLE_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.BUNDLE_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.BUNDLE_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.DAILY_BUMP_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    }
                }


                if (promoType == Promotion.TOP_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.TOP_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.TOP_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.TOP_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.URGENT_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.URGENT_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.URGENT_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.URGENT_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.SPOTLIGHT_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    }
                }

            }

            totalView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(totalSum)));
            if (totalSum > 0) {
                continueBTN.setBackgroundResource(R.drawable.button_round_fb);
                invalid_promos.setVisibility(View.GONE);
            } else {
                continueBTN.setBackgroundResource(R.drawable.button_round_grey);
                invalid_promos.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideAllPromosLayouts() {
        bundle_layout.setVisibility(View.GONE);
        daily_bump_layout.setVisibility(View.GONE);
        top_ad_layout.setVisibility(View.GONE);
        urgent_layout.setVisibility(View.GONE);
        spotlight_layout.setVisibility(View.GONE);
    }

    private void checkout(){
        if(totalSum > 0){
            //place the promotion order


        }
    }

}