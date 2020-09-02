package com.adeasy.advertise.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.FirebaseAuthenticationCallback;
import com.adeasy.advertise.ui.athentication.login;
import com.adeasy.advertise.ui.athentication.register;
import com.adeasy.advertise.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseAuthentication {
    private static final String TAG = "FirebaseAuthentication";
    private FirebaseAuthenticationCallback firebaseAuthenticationCallback;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public FirebaseAuthentication(FirebaseAuthenticationCallback firebaseAuthenticationCallback, Context context) {
        this.firebaseAuthenticationCallback = firebaseAuthenticationCallback;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void signInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (firebaseAuthenticationCallback != null)
                            firebaseAuthenticationCallback.onCompleteSignIn(task);
                    }
                });
    }

    public void createAccount(String reg_email, String reg_password){
        firebaseAuth.createUserWithEmailAndPassword(reg_email, reg_password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      firebaseAuthenticationCallback.onCompleteCreateAccount(task);
                    }
                });
    }

    public void destroy() {
        this.firebaseAuthenticationCallback = null;
    }
}
