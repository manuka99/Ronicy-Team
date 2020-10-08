package com.adeasy.advertise.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/

public class SharedPreferencesManager {

    private static final String CLOUD_MESSAGING_TOKEN = "app_msg_token";
    private static final String NAME = "shared preferences";

    private static Context context;
    private static SharedPreferencesManager sharedPreferencesManager;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public void setCloudMessagingToken(String msg_token) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CLOUD_MESSAGING_TOKEN, msg_token);
            editor.apply();
        }
    }

    public String getCloudMessagingToken() {
        if (context != null)
            return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(CLOUD_MESSAGING_TOKEN, null);
        else
            return null;
    }
}
