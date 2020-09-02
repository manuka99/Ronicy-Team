package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FirebaseAuthenticationCallback {
    public void onCompleteSignIn(@NonNull Task<AuthResult> task);
    public void onCompleteCreateAccount(@NonNull Task<AuthResult> task);
}
