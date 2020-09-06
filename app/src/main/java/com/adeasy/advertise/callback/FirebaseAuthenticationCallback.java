package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface FirebaseAuthenticationCallback {
    public void onCompleteSignIn(@NonNull Task<AuthResult> task);
    public void onCompleteCreateAccount(@NonNull Task<AuthResult> task);
    public void onCompleteUpdateAccount(@NonNull Task<Void> task);
    public void onCompleteForgotPassword(@NonNull Task<Void> task);
}
