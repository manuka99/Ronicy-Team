package com.adeasy.advertise.ui.Promotion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.manager.PromotionManager;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;

public class PromotionMain extends AppCompatActivity implements PromotionCallback {

    private static final String TAG = "PromotionMain";
    private static final String ADVERTISEMENT_ID = "adID";

    PromotionManager promotionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_promotion_main);

        promotionManager = new PromotionManager(this);

        if (getIntent().hasExtra(ADVERTISEMENT_ID)) {
            promotionManager.savePromotions(new Promotion(UniqueIdBasedOnName.Generator("PROMO"), getIntent().getStringExtra(ADVERTISEMENT_ID), Promotion.SPOTLIGHT_AD, 3));
        }
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

}