package com.adeasy.advertise.ui.administration.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CustomClaimsCallback;
import com.adeasy.advertise.manager.CustomAuthTokenManager;
import com.adeasy.advertise.model.CustomClaims;
import com.adeasy.advertise.ui.administration.advertisement.AdvertisementMain;
import com.adeasy.advertise.ui.administration.order.OrdersMain;
import com.adeasy.advertise.ui.advertisement.Advertisement;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class DashboardHome extends AppCompatActivity implements CustomClaimsCallback, View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView name, roles;
    ImageView profileImage;
    CustomAuthTokenManager customAuthTokenManager;
    CustomClaims customClaims;
    String rolesDescription = "";
    LinearLayout advertisement, orders, users, categories, otp, favourite, getInTouch, chat;
    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_dashboard_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null)
            finish();

        name = findViewById(R.id.name);
        roles = findViewById(R.id.roles);
        profileImage = findViewById(R.id.profileImage);

        //tools management
        advertisement = findViewById(R.id.advertisement);
        orders = findViewById(R.id.orders);
        otp = findViewById(R.id.otp);
        categories = findViewById(R.id.categories);
        users = findViewById(R.id.users);

        favourite = findViewById(R.id.favourite);

        chat = findViewById(R.id.chat);

        getInTouch = findViewById(R.id.getInTouch);

        //listeners
        advertisement.setOnClickListener(this);
        orders.setOnClickListener(this);
        otp.setOnClickListener(this);
        categories.setOnClickListener(this);
        users.setOnClickListener(this);
        favourite.setOnClickListener(this);
        chat.setOnClickListener(this);
        getInTouch.setOnClickListener(this);

        customClaims = null;
        customDialogs = new CustomDialogs(this);

        customAuthTokenManager = new CustomAuthTokenManager(this);

        //set name and image
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).fit().centerCrop().into(profileImage);
    }

    @Override
    public void onClick(View view) {
        if (customClaims != null) {
            if (view == advertisement && (customClaims.isAdvertisement_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, AdvertisementMain.class));
            else if (view == orders && (customClaims.isOrder_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, OrdersMain.class));
            else if (view == users && (customClaims.isAdmin() || customClaims.isUser_manager() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else if (view == otp && (customClaims.isAdmin() || customClaims.isUser_manager() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else if (view == categories && (customClaims.isAdvertisement_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else if (view == favourite && (customClaims.isFavourite_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else if (view == getInTouch && (customClaims.isContact_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else if (view == chat && (customClaims.isChat_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
                startActivity(new Intent(this, DashboardHome.class));
            else
                customDialogs.showPermissionDeniedStorage();
        } else
            Toast.makeText(this, "Please wait while we verify you from our server...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleteGetCustomClaims(@NonNull Task<GetTokenResult> task) {
        if (task != null && task.isSuccessful()) {
            customClaims = customAuthTokenManager.mapJsomClaimsToObject(task.getResult().getClaims());
            readClaimsAndUpdateUi();
        } else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        roles.setText(getString(R.string.loading));
        rolesDescription = "";
        customClaims = null;
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified())
            customAuthTokenManager.getCustomClaimsInLoggedinUser();
        else
            customDialogs.showVerifyEmail();
    }

    //just after the claims are received
    private void readClaimsAndUpdateUi() {
        if (customClaims.isAdmin())
            rolesDescription = "System Administrator, ";

        else {
            if (customClaims.isAdvertisement_manager())
                rolesDescription += "Advertisement Manager, ";
            if (customClaims.isOrder_manager())
                rolesDescription += "Order Manager, ";
            if (customClaims.isFavourite_manager())
                rolesDescription += "Favourite Manager, ";
            if (customClaims.isContact_manager())
                rolesDescription += "Contact Manager, ";
            if (customClaims.isChat_manager())
                rolesDescription += "Orders Manager, ";
            if (customClaims.isUser_manager())
                rolesDescription += "User Manager, ";
            if (customClaims.isGuest_admin())
                rolesDescription += "Admin(Read-Only), ";
        }

        if (rolesDescription.length() < 1)
            customDialogs.showPermissionDeniedStorage();
        else
            rolesDescription = rolesDescription.substring(0, rolesDescription.length() - 2);

        roles.setText(rolesDescription);
    }

}