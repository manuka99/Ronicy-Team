package com.adeasy.advertise.manager;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.PhoneAuthenticationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class FirebasePhoneAuthentication extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    private static final String TAG = "FirebasePhoneAuthentica";
    private PhoneAuthenticationCallback callback;

    public FirebasePhoneAuthentication(PhoneAuthenticationCallback newCallback) {
        this.callback = newCallback;
    }

    public void destroy() {
        this.callback = null;
    }

    public void sendMobileVerifycode(String phoneNum, Activity activity) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                this);        // OnVerificationStateChangedCallbacks
    }

    public void linkMobileWithCurrentUser(PhoneAuthCredential credential, FirebaseUser user) {

        user.linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (callback != null)
                            callback.onCompleteLinkingMobileWithUser(task);
                    }
                });

    }

    public void updateMobileWithCurrent(PhoneAuthCredential credential, FirebaseUser user) {

        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (callback != null)
                    callback.onCompleteUpdateMobileWithUser(task);
            }

        });

    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, FirebaseAuth firebaseAuth) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (callback != null)
                            callback.onCompleteSignInWithPhoneAuthCredential(task);
                    }
                });
    }

    public void unlinkPhoneAuth(FirebaseUser user) {

        user.unlink(PhoneAuthProvider.PROVIDER_ID)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (callback != null)
                            callback.onCompleteUnlinkPhoneAuthCredential(task);
                    }
                });
    }


    public void deletePhoneAuthAccout(FirebaseUser user) {

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (callback != null)
                            callback.onCompleteDeletePhoneAuthCredentialUser(task);
                    }
                });

    }

    public void resendVerificationCode(String phoneNumber, Activity activity,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                this,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @Override
    public void onCodeSent(String verificationId,
                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        if (callback != null)
            callback.onCodeSent(verificationId, forceResendingToken);
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        if (callback != null)
            callback.onVerificationCompleted(phoneAuthCredential);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (callback != null)
            callback.onVerificationFailed(e);
    }

}
