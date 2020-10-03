package com.adeasy.advertise.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.FirebaseAuthenticationCallback;
import com.adeasy.advertise.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class FirebaseAuthentication {
    private static final String TAG = "FirebaseAuthentication";
    private FirebaseAuthenticationCallback firebaseAuthenticationCallback;
    private CustomAuthTokenManager customAuthTokenManager;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public FirebaseAuthentication() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.customAuthTokenManager = new CustomAuthTokenManager();
    }

    public FirebaseAuthentication(FirebaseAuthenticationCallback firebaseAuthenticationCallback, Context context) {
        this.firebaseAuthenticationCallback = firebaseAuthenticationCallback;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.customAuthTokenManager = new CustomAuthTokenManager();
    }

    public void signInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            customAuthTokenManager.GetTokenResultAndAddClaims();
                        }
                        if (firebaseAuthenticationCallback != null)
                            firebaseAuthenticationCallback.onCompleteSignIn(task);
                    }
                });
    }

    public void createAccount(final String reg_email, String reg_password, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(reg_email, reg_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (firebaseAuthenticationCallback != null)
                            firebaseAuthenticationCallback.onCompleteCreateAccount(task);
                        if (task.isSuccessful()) {
                            customAuthTokenManager.GetTokenResultAndAddClaims();
                            updateAccountName(name);
                            new ProfileManager().updateProfile(new User(name, firebaseAuth.getCurrentUser()));
                        }
                    }
                });
    }

    public void updateAccountName(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuthenticationCallback.onCompleteUpdateAccount(task);
                    }
                });
    }

    public void forgotPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuthenticationCallback.onCompleteForgotPassword(task);
                    }
                });
    }

    public void destroy() {
        this.firebaseAuthenticationCallback = null;
    }
}
