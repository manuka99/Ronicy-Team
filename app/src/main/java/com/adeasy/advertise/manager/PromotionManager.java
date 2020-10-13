package com.adeasy.advertise.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.model.ApprovedPromotions;
import com.adeasy.advertise.model.Promotion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PromotionManager {

    private static final String TAG = "PromotionManager";
    private static final String PROMOTIONS = "Promotions";
    private static final String APPROVED_PROMOTIONS = "ApprovedPromotions";
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private PromotionCallback promotionCallback;

    public PromotionManager(PromotionCallback promotionCallback) {
        this.promotionCallback = promotionCallback;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.documentReference = firebaseFirestore.collection(PROMOTIONS).document();
    }

    public PromotionManager() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.documentReference = firebaseFirestore.collection(PROMOTIONS).document();
    }

    public void savePromotions(Promotion promotion) {
        firebaseFirestore.collection(PROMOTIONS).document(promotion.getPromoID()).set(promotion, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (promotionCallback != null)
                    promotionCallback.onCompleteSavePromotion(task);
            }
        });
    }

    public void getPromotionById(String promoID) {
        firebaseFirestore.collection(PROMOTIONS).document(promoID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (promotionCallback != null)
                    promotionCallback.onGetPromotionByID(task);
            }
        });
    }

    public void getAdIDsByPromotionType(int promoType) {
        Query query = getAdIDsByPromotionTypeQuery(promoType);
        if (query != null) {
            getAdIDsByPromotionTypeQuery(promoType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Log.i(TAG, "task sent");
                    final List<String> ids = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (ApprovedPromotions promotion : task.getResult().toObjects(ApprovedPromotions.class)) {
                            ids.add(promotion.getAdvertismentID());
                        }
                    } else
                        Log.i(TAG, task.getException().getMessage());

                    if (promotionCallback != null)
                        promotionCallback.onPromotionsListIds(ids, task.getResult().getQuery());
                }
            });
        }
    }

    public Query getAdIDsByPromotionTypeQuery(int promoType) {
        Query query = firebaseFirestore.collection(APPROVED_PROMOTIONS);

        if(promoType == Promotion.DAILY_BUMP_AD)
            return query.whereEqualTo(ApprovedPromotions.stopPromotions_name, false).whereGreaterThan(ApprovedPromotions.dailyPromoPromoExpireTime_name, new Date()).orderBy(ApprovedPromotions.dailyPromoPromoExpireTime_name, Query.Direction.ASCENDING);

        else if(promoType == Promotion.URGENT_AD)
            return query.whereEqualTo(ApprovedPromotions.stopPromotions_name, false).whereGreaterThan(ApprovedPromotions.urgentPromoExpireTime_name, new Date()).orderBy(ApprovedPromotions.urgentPromoExpireTime_name, Query.Direction.ASCENDING);

        else if(promoType == Promotion.SPOTLIGHT_AD)
            return query.whereEqualTo(ApprovedPromotions.stopPromotions_name, false).whereGreaterThan(ApprovedPromotions.spotLightPromoExpireTime_name, new Date()).orderBy(ApprovedPromotions.spotLightPromoExpireTime_name, Query.Direction.ASCENDING);

        else if(promoType == Promotion.TOP_AD)
            return query.whereEqualTo(ApprovedPromotions.stopPromotions_name, false).whereGreaterThan(ApprovedPromotions.topAdPromoExpireTime_name, new Date()).orderBy(ApprovedPromotions.topAdPromoExpireTime_name, Query.Direction.ASCENDING);

        else if(promoType == Promotion.BUNDLE_AD)
            return query.whereEqualTo(ApprovedPromotions.stopPromotions_name, false).whereGreaterThan(ApprovedPromotions.bundleAdPromoExpireTime_name, new Date()).orderBy(ApprovedPromotions.bundleAdPromoExpireTime_name, Query.Direction.ASCENDING);

        else
            return null;
    }

    public void getPendingPromos(String adID){
        FirebaseFirestore.getInstance().collection(PROMOTIONS).whereEqualTo("activated", false).whereEqualTo("reviewed", false).whereEqualTo("approved", false).whereEqualTo("advertisementID", adID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(promotionCallback != null)
                    promotionCallback.onGetPendingPromotionsByADID(task);
            }
        });
    }

    public void getAppliedPromos(String adID){
        FirebaseFirestore.getInstance().collection(APPROVED_PROMOTIONS).document(adID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(promotionCallback != null)
                    promotionCallback.onGetAppliedApprovedPromotionByADID(task);
            }
        });
    }

    public void destroy() {
        this.promotionCallback = null;
    }

}
