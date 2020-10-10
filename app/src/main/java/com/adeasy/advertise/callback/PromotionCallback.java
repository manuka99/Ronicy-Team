package com.adeasy.advertise.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface PromotionCallback {

    public void onCompleteSavePromotion(Task<Void> task);
    public void onGetPromotionByID(Task<DocumentSnapshot> task);
    public void onPromotionsListIds(List<String> ids, Query promotionQuery);

}
