package com.adeasy.advertise.callback;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface CategoryCallback {
    //public void onUploadImage(@NonNull Task<Uri> task);
    //public void onTaskFull(boolean result);
    //public void onSuccessInsertCategory();
    //public void onFailureInsertCategory();
    //public void onSuccessDeleteCategory();
    //public void onFailureDeleteCategory();
    //public void onSuccessUpdateCategory();
    //public void onFailureUpdateCategory();
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task);
}
