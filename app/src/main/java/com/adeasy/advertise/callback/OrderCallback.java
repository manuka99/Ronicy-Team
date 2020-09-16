package com.adeasy.advertise.callback;

import android.app.TaskInfo;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface OrderCallback {
    public void onCompleteInsertOrder(Task<Void> task);
    public void onGetOrderByID(Task<DocumentSnapshot> task);
    public void onHideOrderByID(Task<Void> task);
}
