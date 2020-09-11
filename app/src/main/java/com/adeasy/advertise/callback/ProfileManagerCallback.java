package com.adeasy.advertise.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface ProfileManagerCallback {
    public void onSuccessUpdateProfile(Void aVoid);
    public void onFailureUpdateProfile(Exception e);
    public void onCompleteUpdatePassword(Task<Void> task);
    public void onCompleteUpdateEmail(Task<Void> task);
    public void onTaskFull(boolean status);
    public void onCompleteGetUser(Task<DocumentSnapshot> task);
}
