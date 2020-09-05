package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FacebookAuthCallback {
    public void onCompleteSignInWithFacebook(@NonNull Task<AuthResult> task);
    //public void onDataFromTokenRecieved(String email);
    public void onCompleteLinkInWithFacebook(@NonNull Task<AuthResult> task);
    public void onCompleteUnlinkFacebookAuthCredential(@NonNull Task<AuthResult> task);
}
