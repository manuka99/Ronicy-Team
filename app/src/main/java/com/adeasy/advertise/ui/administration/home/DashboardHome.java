package com.adeasy.advertise.ui.administration.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CustomClaimsCallback;
import com.adeasy.advertise.manager.CustomAuthTokenManager;
import com.adeasy.advertise.model.CustomClaims;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DashboardHome extends AppCompatActivity implements CustomClaimsCallback, View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView name, roles;
    ImageView profileImage;
    CustomAuthTokenManager customAuthTokenManager;
    CustomClaims customClaims;
    String rolesDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_dashboard_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null)
            finish();

        name = findViewById(R.id.name);
        roles = findViewById(R.id.roles);
        profileImage = findViewById(R.id.profileImage);

        customClaims = new CustomClaims();

        customAuthTokenManager = new CustomAuthTokenManager(this);
        customAuthTokenManager.getCustomClaimsInLoggedinUser();

        //set name and image
        name.setText(firebaseUser.getDisplayName());
        Picasso.get().load(firebaseUser.getPhotoUrl()).into(profileImage);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCompleteGetCustomClaims(@NonNull Task<GetTokenResult> task) {
        if(task.isSuccessful()){
            customClaims = customAuthTokenManager.mapJsomClaimsToObject(task.getResult().getClaims());
            readClaimsAndUpdateUi();
        }else{

        }
    }

    //just after the claims are received
    private void readClaimsAndUpdateUi(){
            if(customClaims.isAdmin())
                rolesDescription = "System Administrator";

            else{
                if(customClaims.isOrder_manager())
                    rolesDescription += "Order Manager, ";
                if(customClaims.isAdvertisement_manager())
                    rolesDescription += "Advertisement Manager, ";
                if(customClaims.isFavourite_manager())
                    rolesDescription += "Favourite Manager, ";
                if(customClaims.isContact_manager())
                    rolesDescription += "Contact Manager, ";
                if(customClaims.isChat_manager())
                    rolesDescription += "Chat Manager, ";
            }

            if(rolesDescription == null)
                finish();

            roles.setText(rolesDescription);
    }

}