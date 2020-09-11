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

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CustomClaimsCallback;
import com.adeasy.advertise.manager.CustomAuthTokenManager;
import com.adeasy.advertise.model.CustomClaims;
import com.adeasy.advertise.ui.administration.advertisement.AdvertisementMain;
import com.adeasy.advertise.ui.advertisement.Advertisement;
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
    String rolesDescription = "";
    LinearLayout advertisement, orders, users, categories, otp, favourite, getInTouch, chat;

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

        customClaims = new CustomClaims();

        customAuthTokenManager = new CustomAuthTokenManager(this);
        customAuthTokenManager.getCustomClaimsInLoggedinUser();

        //set name and image
        name.setText(firebaseUser.getDisplayName());
        Picasso.get().load(firebaseUser.getPhotoUrl()).fit().centerInside().into(profileImage);
    }

    @Override
    public void onClick(View view) {
        if (view == advertisement && (customClaims.isAdvertisement_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
            startActivity(new Intent(this, AdvertisementMain.class));
        else if (view == orders && (customClaims.isOrder_manager() || customClaims.isAdmin() || customClaims.isGuest_admin()))
            startActivity(new Intent(this, DashboardHome.class));
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
            showErrorDialog();
    }

    @Override
    public void onCompleteGetCustomClaims(@NonNull Task<GetTokenResult> task) {
        if (task.isSuccessful()) {
            customClaims = customAuthTokenManager.mapJsomClaimsToObject(task.getResult().getClaims());
            readClaimsAndUpdateUi();
        } else {

        }
    }

    private void showErrorDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Opps! Access denied /404");
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setText(firebaseUser.getDisplayName() + " the content that you requested may not be available for you. You can only request content that matches your role, if you think this is by mistake please sign in again. Thank you!");
        Button signOut = (Button) dialog.findViewById(R.id.button);
        signOut.setText("Sign Out");
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
            }
        });
        TextView close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //just after the claims are received
    private void readClaimsAndUpdateUi() {
        if (customClaims.isAdmin())
            rolesDescription = "System Administrator, ";

        else {
            if (customClaims.isOrder_manager())
                rolesDescription += "Order Manager, ";
            if (customClaims.isAdvertisement_manager())
                rolesDescription += "Advertisement Manager, ";
            if (customClaims.isFavourite_manager())
                rolesDescription += "Favourite Manager, ";
            if (customClaims.isContact_manager())
                rolesDescription += "Contact Manager, ";
            if (customClaims.isChat_manager())
                rolesDescription += "Chat Manager, ";
        }

        if (rolesDescription.length() < 1)
            finish();
        else
            rolesDescription = rolesDescription.substring(0, rolesDescription.length() - 2);

        roles.setText(rolesDescription);
    }

}