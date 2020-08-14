package com.adeasy.advertise.firebase;

import android.content.Context;
import android.net.Uri;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public interface CategoryFirebase {

    static final String TAG = "CategoryFirebase";

    public void uploadImage(Category category, byte[] data, Context context);
    public void insertCat(Category category, Context context);
    public void updateCat();
    public void deleteCat();
    public Query viewCats();
    public DocumentReference getCatbyID(String id);

}
