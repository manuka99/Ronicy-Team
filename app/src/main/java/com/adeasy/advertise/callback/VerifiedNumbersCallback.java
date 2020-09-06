package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface VerifiedNumbersCallback {
    public void onSuccessfullNumberInserted();
    public void onCompleteSearchNumberInUser(QuerySnapshot querySnapshotTask);
    public void onCompleteRecieveAllNumbersInUser(DocumentSnapshot documentSnapshot);
}
