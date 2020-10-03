package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface CustomClaimsCallback {
    public void onCompleteGetCustomClaims(@NonNull Task<GetTokenResult> task);
}
