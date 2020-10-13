package com.adeasy.advertise.ui.Promotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.manager.PromotionManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PromotionHistory extends AppCompatActivity implements PromotionCallback, AdvertisementCallback {

    public static final String ADVERTISEMENT_ID = "adID";
    String advertisementID;
    PromotionManager promotionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_promotion_history);

        if (getIntent().hasExtra(ADVERTISEMENT_ID))
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);

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

    }

    @Override
    public void onGetPendingPromotionsByADID(Task<QuerySnapshot> task) {

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

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }
    
}