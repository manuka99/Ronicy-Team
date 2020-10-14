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
            //get the id token from the auth object
            firebaseAuth.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Send token to backend via HTTPS
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
        String url = Configurations.SERVER_URL_AUTH_TOKEN + idToken;

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
                    Log.i(TAG, task.toString());
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
            for (String claim : claims.keySet()) {
                if (claim.equals(CustomClaims.ADMIN))
                    customClaims.setAdmin(true);
                if (claim.equals(CustomClaims.ADVERTISEMENT_MANAGER))
                    customClaims.setAdvertisement_manager(true);
                if (claim.equals(CustomClaims.ORDER_MANAGER))
                    customClaims.setOrder_manager(true);
                if (claim.equals(CustomClaims.FAVOURITE_MANAGER))
                    customClaims.setFavourite_manager(true);
                if (claim.equals(CustomClaims.CHAT_MANAGER))
                    customClaims.setChat_manager(true);
                if(claim.equals(CustomClaims.CONTACT_MANAGER))
                    customClaims.setContact_manager(true);
                if(claim.equals(CustomClaims.USER_MANAGER))
                    customClaims.setUser_manager(true);
                if(claim.equals(CustomClaims.GUEST_ADMIN))
                    customClaims.setGuest_admin(true);
            }
        } catch (Exception e) {
            Log.i(TAG, "Claims were not successfull");
            e.printStackTrace();
        }
        return customClaims;
    }

}