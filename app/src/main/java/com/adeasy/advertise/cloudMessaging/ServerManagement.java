package com.adeasy.advertise.cloudMessaging;

import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.config.Configurations;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.adeasy.advertise.config.Configurations.SERVER_URL_ADMIN_FCM_UID;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ServerManagement implements OnCompleteListener<String>{

    private static final String TAG = "ServerManagement";
    private int type;
    private String uid;

    public static final int TYPE_PUBLIC = 1;
    public static final int TYPE_ADMIN = 0;

    public ServerManagement(int type, String uid){
        this.type = type;
        this.uid = uid;
    }

    public void subscribeToThePublicFCM(String token) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Configurations.SERVER_URL_PUBLIC_FCM + token;

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

    public void subscribeToTheAdministrationFCM(String token, String uid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Configurations.SERVER_URL_ADMIN_FCM + token + SERVER_URL_ADMIN_FCM_UID + uid;

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

    public void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<String> task) {
        if (!task.isSuccessful()) {

            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            return;
        }

        String token = task.getResult();

        if(this.type == TYPE_PUBLIC)
            subscribeToThePublicFCM(token);

        if(this.type == TYPE_ADMIN && uid != null)
            subscribeToTheAdministrationFCM(token, this.uid);
    }

}
