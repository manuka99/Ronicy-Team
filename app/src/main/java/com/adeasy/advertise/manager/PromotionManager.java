package com.adeasy.advertise.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.PromotionCallback;
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
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private PromotionCallback promotionCallback;

    public PromotionManager(PromotionCallback promotionCallback) {
        this.promotionCallback = promotionCallback;
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
       getAdIDsByPromotionTypeQuery(promoType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i(TAG, "task sent");
                final List<String> ids = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (Promotion promotion : task.getResult().toObjects(Promotion.class)) {
                        ids.add(promotion.getAdvertisementID());
                    }
                } else
                    Log.i(TAG, task.getException().getMessage());

                if (promotionCallback != null)
                    promotionCallback.onPromotionsListIds(ids, task.getResult().getQuery());
            }
        });
    }

    public Query getAdIDsByPromotionTypeQuery(int promoType){
        return  firebaseFirestore.collection(PROMOTIONS).whereEqualTo("promotionType", promoType).whereEqualTo("approved", true).whereGreaterThan("expireDate", new Date()).orderBy("expireDate", Query.Direction.ASCENDING);
    }

    public void destroy() {
        this.promotionCallback = null;
    }

}
