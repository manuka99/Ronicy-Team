package com.adeasy.advertise.ui.Promotion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.PromotionsViewModel;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.manager.PromotionManager;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionMain extends AppCompatActivity implements PromotionCallback, View.OnClickListener {

    private static final String TAG = "PromotionMain";
    private static final String PROMOS_ADDED = "promos_added";
    private static final String ADVERTISEMENT_ID = "adID";

    PromotionManager promotionManager;

    Toolbar toolbar;
    LinearLayout promotionsLayout;
    Map<Integer, Integer> promos;

    //promotions Fragments
    BundleAds bundleAds;
    DailyBump dailyBump;
    TopAds topAds;
    UrgentAds urgentAds;
    SpotLightAd spotLightAd;

    //viewModel
    PromotionsViewModel promotionsViewModel;

    String advertisementID;

    Button continueBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_promotion_main);

        if (getIntent().hasExtra(ADVERTISEMENT_ID))
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);
        else
            finish();

        //toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Promotions");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        promotionsLayout = findViewById(R.id.promotionsLayout);
        continueBTN = findViewById(R.id.continueBTN);

        //initialize fragments
        initializeFragments();

        promotionManager = new PromotionManager(this);

        promotionsViewModel = ViewModelProviders.of(this).get(PromotionsViewModel.class);

        promos = new HashMap<>();
        continueBTN.setOnClickListener(this);
        //load pending and approved promotions

        //load all promotions
        loadAllPromotions();
        changeColourOfContinueBTN();
    }

    private void loadAllPromotions() {
        initializeFragments();
        promotionsLayout.removeAllViews();
        getSupportFragmentManager().beginTransaction().add(promotionsLayout.getId(), bundleAds)
                .add(promotionsLayout.getId(), dailyBump)
                .add(promotionsLayout.getId(), topAds)
                .add(promotionsLayout.getId(), urgentAds)
                .add(promotionsLayout.getId(), spotLightAd).commit();
    }

    private void initializeFragments() {
        bundleAds = new BundleAds();
        dailyBump = new DailyBump();
        topAds = new TopAds();
        urgentAds = new UrgentAds();
        spotLightAd = new SpotLightAd();
    }

    @Override
    public void onCompleteSavePromotion(Task<Void> task) {
        if (task != null) {
            if (task.isSuccessful()) {
                Log.i(TAG, "success");
            } else {
                Log.i(TAG, "error");
                Log.i(TAG, task.getException().getMessage());
            }
        } else {
            Log.i(TAG, "no document");
        }
    }

    @Override
    public void onGetPromotionByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onPromotionsListIds(List<String> ids, Query promotionQuery) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        promotionsViewModel.getSelectedPromo().observe(this, new Observer<Map<Integer, Integer>>() {
            @Override
            public void onChanged(Map<Integer, Integer> integerIntegerMap) {
                Log.i(TAG, "updating promos");
                promos.putAll(integerIntegerMap);
                changeColourOfContinueBTN();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == continueBTN && validateContinueBTN()) {
            for (Integer promo : promos.keySet()) {
                Log.i(TAG, "type: " + promo + " days: " + promos.get(promo));
            }
            Intent intent = new Intent(this, Payment.class);
            intent.putExtra(PROMOS_ADDED, (Serializable) promos);
            intent.putExtra(ADVERTISEMENT_ID, advertisementID);
            startActivity(intent);
        }
    }

    private boolean validateContinueBTN() {
        if (promos != null) {
            for (Integer value : promos.values()) {
                if (value == 3 || value == 7 || value == 15)
                    return true;
            }
        }
        return false;
    }

    private void changeColourOfContinueBTN() {
        if (validateContinueBTN())
            continueBTN.setBackgroundResource(R.drawable.button_round_fb);
        else
            continueBTN.setBackgroundResource(R.drawable.button_round_grey);
    }

}