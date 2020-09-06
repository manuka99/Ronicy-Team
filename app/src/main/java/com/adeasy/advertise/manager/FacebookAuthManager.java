package com.adeasy.advertise.manager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.FacebookAuthCallback;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class FacebookAuthManager {

    private FacebookAuthCallback facebookAuthCallback;
    private static final String TAG = "FacebookAuthManager";
    private FirebaseAuth firebaseAuth;

    public FacebookAuthManager(FacebookAuthCallback facebookAuthCallback) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.facebookAuthCallback = facebookAuthCallback;
    }

    public void handleFacebookAccessToken(AccessToken token) {
        //getAllDataFromToken(token);
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (facebookAuthCallback != null)
                            facebookAuthCallback.onCompleteSignInWithFacebook(task);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            AuthCredential credential1 = EmailAuthProvider.getCredential(user.getEmail(), UUID.randomUUID().toString());
                            user.linkWithCredential(credential1);
                        }
                    }
                });
    }

    public void linkFacebookToUser(AccessToken accessToken) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (facebookAuthCallback != null)
                    facebookAuthCallback.onCompleteLinkInWithFacebook(task);
            }
        });
    }

    public void unlinkFromFBProvider() {
        firebaseAuth.getCurrentUser().unlink(FacebookAuthProvider.PROVIDER_ID)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        facebookAuthCallback.onCompleteUnlinkFacebookAuthCredential(task);
                    }
                });
    }

    private void getAllDataFromTokenAndCreateAccount(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                final JSONObject json = response.getJSONObject();
                String email = null;

                try {
                    if (json != null) {
                        //String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                    /*details_txt.setText(Html.fromHtml(text));
                    profile.setProfileId(json.getString("id"));*/

                        Log.e(TAG, json.getString("name"));
                        Log.e(TAG, json.getString("email"));
                        Log.e(TAG, json.getString("id"));
                        email = json.getString("id");
                        //web.loadData(text, "text/html", "UTF-8");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //facebookAuthCallback.onDataFromTokenRecieved(email);
            }
        });
    }

    public void destroy() {
        this.firebaseAuth = null;
        this.facebookAuthCallback = null;
    }

}
