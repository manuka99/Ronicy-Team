package com.adeasy.advertise.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.CustomClaimsCallback;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.model.CustomClaims;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class CustomAuthTokenManager {

    private static final String TAG = "CustomAuthTokenManager";
    private CustomClaimsCallback customClaimsCallback;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public CustomAuthTokenManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public CustomAuthTokenManager(CustomClaimsCallback customClaimsCallback) {
        this.customClaimsCallback = customClaimsCallback;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void GetTokenResultAndAddClaims() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Send token to your backend via HTTPS
                                requestCustomClaimsToken(idToken);

                                Log.i(TAG, idToken);

                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }
    }

    private void requestCustomClaimsToken(String idToken) {
        Log.i(TAG, idToken);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Configurations.SERVER_URL + idToken;

        Log.i(TAG, url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        Log.i(TAG, response);
                        //logingWithToken(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
                error.printStackTrace();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void logingWithToken(String token) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseAuth.signOut();
            firebaseAuth.signInWithCustomToken(token).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        task.getResult().getUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                            @Override
                            public void onSuccess(GetTokenResult result) {
                                Log.i(TAG, result.getClaims().toString());
                                boolean isAdmin = (boolean) result.getClaims().get("admin");
                                if (isAdmin) {
                                    // Show admin UI.
                                    Log.i(TAG, "admin account");
                                } else {
                                    // Show regular user UI.
                                    Log.i(TAG, "not a admin account");
                                }
                            }
                        });
                    } else
                        Log.i(TAG, task.getException().getMessage());
                }
            });
        }
    }

    public void getCustomClaimsInLoggedinUser() {
        if (firebaseUser != null) {
            firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (customClaimsCallback != null)
                        customClaimsCallback.onCompleteGetCustomClaims(task);
                }
            });
        } else {
            Log.i(TAG, "user is not logged in");
            if (customClaimsCallback != null)
                customClaimsCallback.onCompleteGetCustomClaims(null);
        }
    }

    public CustomClaims mapJsomClaimsToObject(Map<String, Object> claims) {
        CustomClaims customClaims = new CustomClaims();
        try {
            customClaims.setAdmin((Boolean) claims.get("admin"));
            customClaims.setAdvertisement_manager((Boolean) claims.get("advertisement_manager"));
            customClaims.setOrder_manager((Boolean) claims.get("order_manager"));
            customClaims.setFavourite_manager((Boolean) claims.get("favourite_manager"));
            customClaims.setChat_manager((Boolean) claims.get("chat_manager"));
            customClaims.setContact_manager((Boolean) claims.get("contact_manager"));
            customClaims.setUser_manager((Boolean) claims.get("user_manager"));
            customClaims.setGuest_admin((Boolean) claims.get("guest_admin"));
        } catch (Exception e) {
            Log.i(TAG, "Claims were not successfull");
            e.printStackTrace();
        }
        return customClaims;
    }

}