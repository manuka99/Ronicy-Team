package com.adeasy.advertise.firebase;

import android.content.Context;
import android.net.Uri;

import com.adeasy.advertise.model.Advertisement;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public interface AdvertisementFirebase {

    static final String TAG = "AdvertisementFirebase";

    public void uploadImage(Advertisement advertisement, byte[] data, Context context);
    public void insertAdd(Advertisement advertisement, Context context);
    public void updateAdd();
    public void deleteAdd(String id, final String url, Context context);
    public void hideAdd(String id, boolean visibility, Context context);
    public Query viewAdds();
    public Query viewAddsAll();
    public DocumentReference getAddbyID(String id);

}
