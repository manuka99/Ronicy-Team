package com.adeasy.advertise.callback;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface PhoneAuthenticationCallback {

    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken);
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential);
    public void onVerificationFailed(FirebaseException e);
    public void onCompleteLinkingMobileWithUser(@NonNull Task<AuthResult> task);
    public void onCompleteUpdateMobileWithUser(@NonNull Task<Void> task);
    public void onCompleteSignInWithPhoneAuthCredential(@NonNull Task<AuthResult> task);
    public void onCompleteUnlinkPhoneAuthCredential(@NonNull Task<AuthResult> task);
    public void onCompleteDeletePhoneAuthCredentialUser(@NonNull Task<Void> task);

}
