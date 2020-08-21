package com.adeasy.advertise.callback;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.adeasy.advertise.model.Advertisement;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public interface AdvertisementCallback {
    public void onUploadImage(@NonNull Task<Uri> task);
    public void onTaskFull(boolean result);
    public void onSuccessInsertAd();
    public void onFailureInsertAd();
    public void onSuccessDeleteAd();
    public void onFailureDeleteAd();
    public void onSuccessUpdatetAd();
    public void onFailureUpdateAd();
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task);
}
