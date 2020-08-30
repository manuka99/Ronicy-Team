package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface VerifiedNumbersCallback {
    public void onCompleteSearchNumberInUser(QuerySnapshot querySnapshotTask);
    public void onCompleteRecieveAllNumbersInUser(QuerySnapshot querySnapshotTask);
}
