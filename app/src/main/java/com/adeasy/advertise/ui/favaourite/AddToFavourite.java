package com.adeasy.advertise.ui.favaourite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Favourite;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.UUID;


public class AddToFavourite extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String adID;
    Context context;

    private static final String TAG = "addToFavourite";
    private static final String ADVERTISEMENTID = "adID";
    private static final String FAVOURITE_COLLECTION = "Favourites";


    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divya_activity_add_to_favourite);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        customDialogs = new CustomDialogs(this);
        context = this;

        if (!new InternetValidation().validateInternet(context))
            customDialogs.showNoInternetDialog();

        if (firebaseAuth.getCurrentUser() == null)
            customDialogs.showPermissionDeniedStorage();

        try {
            if (getIntent().getStringExtra(ADVERTISEMENTID) != null)
                adID = getIntent().getStringExtra(ADVERTISEMENTID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseUser = firebaseAuth.getCurrentUser();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Adding to favorite");
        progressDialog.setMessage("please wait.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (adID != null && firebaseUser != null) {

            firebaseFirestore.collection(FAVOURITE_COLLECTION).whereEqualTo("advertisementID", adID).whereEqualTo("userID", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "already added", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String id = UUID.randomUUID().toString().replace("-", "");
                            firebaseFirestore.collection(FAVOURITE_COLLECTION).document(id).set(new Favourite(id, adID, firebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "added To favourite", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    if (!task.isSuccessful() || task.getException() instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) task.getException()).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
                                        customDialogs.showPermissionDeniedStorage();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(context, "Server error *", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });

        }

    }
}