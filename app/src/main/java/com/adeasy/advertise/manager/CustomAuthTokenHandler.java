package com.adeasy.advertise.manager;

import android.util.Log;

import androidx.annotation.NonNull;

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

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomAuthTokenHandler {

    private static final String TAG = "CustomAuthTokenHandler";

    public void GetTokenResultAndAddClaims() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Send token to your backend via HTTPS
                                requestCustomClaimsToken(idToken);
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
        String url = "http://manuka-42212.portmap.host:42212/user/get_token?tokenID=" + idToken;

        Log.i(TAG, url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        Log.i(TAG, response);
                        logingWithToken(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
                Log.i(TAG, error.getMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void logingWithToken(String token) {

        Log.i(TAG, token);

        FirebaseAuth.getInstance().signInWithCustomToken(token).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    task.getResult().getUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult result) {
                            boolean isAdmin = (boolean) result.getClaims().get("premiumAccount");
                            if (isAdmin) {
                                // Show admin UI.
                                Log.i(TAG, "premium acount");
                            } else {
                                // Show regular user UI.
                                Log.i(TAG, "not a premium acount");
                            }
                        }
                    });
                } else
                    Log.i(TAG, task.getException().getMessage());
            }
        });
    }

}
