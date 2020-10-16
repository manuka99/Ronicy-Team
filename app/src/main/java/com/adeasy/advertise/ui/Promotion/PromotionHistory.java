package com.adeasy.advertise.ui.Promotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.manager.PromotionManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.ApprovedPromotions;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Promotion;
import com.github.javafaker.App;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PromotionHistory extends AppCompatActivity implements PromotionCallback, AdvertisementCallback, CategoryCallback {

    public static final String ADVERTISEMENT_ID = "adID";
    String advertisementID;

    //managers
    PromotionManager promotionManager;
    AdvertisementManager advertisementManager;
    CategoryManager categoryManager;

    Toolbar toolbar;

    //for ad
    ImageView imageView;
    TextView title, category, price;
    Advertisement advertisement;


    //main content
    LinearLayout main_content;
    ProgressBar progressBarMain, progressPendingPromos, progressAppliedPromos;

    //promotions history
    TextView no_applied_promos, no_pending_promos;
    LinearLayout appliedPromosLayout, pendingPromosLayout;

    boolean isAdLoaded = false;
    boolean isCategoryLoaded = false;
    boolean isAppliedPromosLoaded = false;
    boolean isPendingPromosLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_promotion_history);

        if (getIntent().hasExtra(ADVERTISEMENT_ID))
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);

        //ad layout
        title = findViewById(R.id.title);
        no_applied_promos = findViewById(R.id.no_applied_promos);
        no_pending_promos = findViewById(R.id.no_pending_promos);
        progressAppliedPromos = findViewById(R.id.progressAppliedPromos);
        progressPendingPromos = findViewById(R.id.progressPendingPromos);
        main_content = findViewById(R.id.main_content);
        progressBarMain = findViewById(R.id.progressBarMain);
        imageView = findViewById(R.id.adImage);
        category = findViewById(R.id.category);
        price = findViewById(R.id.adPrice);
        appliedPromosLayout = findViewById(R.id.appliedPromos);
        pendingPromosLayout = findViewById(R.id.pendingPromos);

        //hide layouts
        main_content.setVisibility(View.GONE);
        no_applied_promos.setVisibility(View.GONE);
        no_pending_promos.setVisibility(View.GONE);
        progressBarMain.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Promotions History");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        promotionManager = new PromotionManager(this);
        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);

        if (advertisementID != null) {
            advertisementManager.getAddbyID(advertisementID);
            promotionManager.getAppliedPromos(advertisementID);
            promotionManager.getPendingPromos(advertisementID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        validateAllDataLoaded();
    }

    @Override
    public void onCompleteSavePromotion(Task<Void> task) {

    }

    @Override
    public void onGetPromotionByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onPromotionsListIds(List<String> ids, Query promotionQuery) {

    }

    @Override
    public void onGetAppliedApprovedPromotionByADID(Task<DocumentSnapshot> task) {

        isAppliedPromosLoaded = true;
        validateAllDataLoaded();

        boolean noAppliedPromos = true;
        if (task != null && task.isSuccessful() && task.getResult().exists()) {

            ApprovedPromotions approvedPromotions = task.getResult().toObject(ApprovedPromotions.class);

            if (approvedPromotions.getBundleAdPromoExpireTime() != null && approvedPromotions.getBundleAdPromoExpireTime().after(new Date())) {
                noAppliedPromos = false;
                appliedPromosLayout.addView(bundleAdTextView("Bundle Ad Package expires on " + approvedPromotions.getBundleAdPromoExpireTime()));
            }

            if (approvedPromotions.getDailyPromoPromoExpireTime() != null && approvedPromotions.getDailyPromoPromoExpireTime().after(new Date())) {
                noAppliedPromos = false;
                appliedPromosLayout.addView(dailyAdTextView("Daily Boast Ad Package expires on " + approvedPromotions.getDailyPromoPromoExpireTime()));
            }

            if (approvedPromotions.getTopAdPromoExpireTime() != null && approvedPromotions.getTopAdPromoExpireTime().after(new Date())) {
                noAppliedPromos = false;
                appliedPromosLayout.addView(topAdTextView("Top Ad Package expires on " + approvedPromotions.getTopAdPromoExpireTime()));
            }

            if (approvedPromotions.getUrgentPromoExpireTime() != null && approvedPromotions.getUrgentPromoExpireTime().after(new Date())) {
                noAppliedPromos = false;
                appliedPromosLayout.addView(urgentAdTextView("Urgent Ad Package expires on " + approvedPromotions.getUrgentPromoExpireTime()));
            }

            if (approvedPromotions.getSpotLightPromoExpireTime() != null && approvedPromotions.getSpotLightPromoExpireTime().after(new Date())) {
                noAppliedPromos = false;
                appliedPromosLayout.addView(spotlightAdTextView("Spotlight Ad Package expires on " + approvedPromotions.getSpotLightPromoExpireTime()));
            }

        }

        if (noAppliedPromos)
            no_applied_promos.setVisibility(View.VISIBLE);
        else
            no_applied_promos.setVisibility(View.GONE);

        main_content.setVisibility(View.VISIBLE);
        progressAppliedPromos.setVisibility(View.GONE);

    }

    @Override
    public void onGetPendingPromotionsByADID(Task<QuerySnapshot> task) {

        isPendingPromosLoaded = true;
        validateAllDataLoaded();

        boolean noPendingPromos = true;

        if (task != null && task.isSuccessful() && task.getResult().size() > 0) {

            noPendingPromos = false;

            List<Promotion> promotions = task.getResult().toObjects(Promotion.class);

            for (Promotion promotion : promotions) {

                if (promotion.getPromos() != null) {

                    for (String promoType : promotion.getPromos().keySet()) {

                        if (promoType.equals(String.valueOf(Promotion.BUNDLE_AD))) {
                            pendingPromosLayout.addView(bundleAdTextView("Bundle Ad Package added on " + promotion.getPlaceDate() + " for" + promotion.getPromos().get(promoType) + " days."));
                        }

                        if (promoType.equals(String.valueOf(Promotion.DAILY_BUMP_AD))) {
                            pendingPromosLayout.addView(dailyAdTextView("Daily Boast Ad Package added on " + promotion.getPlaceDate() + " for" + promotion.getPromos().get(promoType) + " days."));
                        }

                        if (promoType.equals(String.valueOf(Promotion.TOP_AD))) {
                            pendingPromosLayout.addView(topAdTextView("Top Ad Package added on " + promotion.getPlaceDate() + " for" + promotion.getPromos().get(promoType) + " days."));
                        }

                        if (promoType.equals(String.valueOf(Promotion.URGENT_AD))) {
                            pendingPromosLayout.addView(urgentAdTextView("Urgent Ad Package added on " + promotion.getPlaceDate() + " for" + promotion.getPromos().get(promoType) + " days."));
                        }

                        if (promoType.equals(String.valueOf(Promotion.SPOTLIGHT_AD))) {
                            pendingPromosLayout.addView(spotlightAdTextView("Spotlight Ad Package added on " + promotion.getPlaceDate() + " for" + promotion.getPromos().get(promoType) + " days."));
                        }

                    }

                }

            }

        }

        if (noPendingPromos)
            no_pending_promos.setVisibility(View.VISIBLE);
        else
            no_pending_promos.setVisibility(View.GONE);

        main_content.setVisibility(View.VISIBLE);
        progressPendingPromos.setVisibility(View.GONE);

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

        isAdLoaded = true;
        validateAllDataLoaded();

        if (task != null && task.isSuccessful()) {
            advertisement = task.getResult().toObject(Advertisement.class);

            //get the category details
            categoryManager.getCategorybyID(advertisement.getCategoryID());

            //add layouts and views
            title.setText(advertisement.getTitle());
            Picasso.get().load(advertisement.getImageUrls().get(0)).into(imageView);
            price.setText(advertisement.getPreetyCurrency());
        }
        main_content.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {

        isCategoryLoaded = true;
        validateAllDataLoaded();

        if (task != null && task.isSuccessful()) {
            Category cc = task.getResult().toObject(Category.class);
            category.setText(cc.getName());
        }
        main_content.setVisibility(View.VISIBLE);

    }

    private void validateAllDataLoaded() {
        if (isAdLoaded && isCategoryLoaded && isAppliedPromosLoaded && isPendingPromosLoaded)
            progressBarMain.setVisibility(View.GONE);
        else
            progressBarMain.setVisibility(View.VISIBLE);
    }

    private View bundleAdTextView(String des) {
        ViewGroup bundleAds = (ViewGroup) getLayoutInflater().inflate(R.layout.manuka_bundle_ad_2_columns, null);
        LinearLayout linearLayout = bundleAds.findViewById(R.id.mainLayout);
        ((TextView) linearLayout.findViewById(R.id.description)).setText(des);
        return linearLayout;
    }

    private View dailyAdTextView(String des) {
        ViewGroup bundleAds = (ViewGroup) getLayoutInflater().inflate(R.layout.manuka_daily_ad_2_columns, null);
        LinearLayout linearLayout = bundleAds.findViewById(R.id.mainLayout);
        ((TextView) linearLayout.findViewById(R.id.description)).setText(des);
        return linearLayout;
    }

    private View topAdTextView(String des) {
        ViewGroup bundleAds = (ViewGroup) getLayoutInflater().inflate(R.layout.manuka_top_ad_2_columns, null);
        LinearLayout linearLayout = bundleAds.findViewById(R.id.mainLayout);
        ((TextView) linearLayout.findViewById(R.id.description)).setText(des);
        return linearLayout;
    }

    private View urgentAdTextView(String des) {
        ViewGroup bundleAds = (ViewGroup) getLayoutInflater().inflate(R.layout.manuka_urgent_ad_2_columns, null);
        LinearLayout linearLayout = bundleAds.findViewById(R.id.mainLayout);
        ((TextView) linearLayout.findViewById(R.id.description)).setText(des);
        return linearLayout;
    }

    private View spotlightAdTextView(String des) {
        ViewGroup bundleAds = (ViewGroup) getLayoutInflater().inflate(R.layout.manuka_spotlight_ad_2_columns, null);
        LinearLayout linearLayout = bundleAds.findViewById(R.id.mainLayout);
        ((TextView) linearLayout.findViewById(R.id.description)).setText(des);
        return linearLayout;
    }

}