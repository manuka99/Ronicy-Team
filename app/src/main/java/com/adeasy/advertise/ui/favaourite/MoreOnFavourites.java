package com.adeasy.advertise.ui.favaourite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Favourite;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.HideSoftKeyboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class MoreOnFavourites extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MoreOnFavourites";
    public static final String FAVOURITE_ID = "fav_id";

    String favouriteID;
    FirebaseFirestore firebaseFirestore;
    Toolbar toolbar;
    EditText edit_text;
    Button saveBTN;
    Favourite favourite;
    CustomDialogs customDialogs;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divya_activity_more_on_favourites);

        frameLayout = findViewById(R.id.frameLayout);
        toolbar = findViewById(R.id.toolbar);
        edit_text = findViewById(R.id.edit_text);
        saveBTN = findViewById(R.id.saveBTN);
        customDialogs = new CustomDialogs(this);

        //listeners
        saveBTN.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().hasExtra(FAVOURITE_ID))
            favouriteID = getIntent().getStringExtra(FAVOURITE_ID);

        firebaseFirestore = FirebaseFirestore.getInstance();
        loadData();
    }

    private void loadData() {
        if (favouriteID != null) {
            Log.i(TAG, "data sent " + favouriteID);
            firebaseFirestore.collection(Favourite.COLLECTION_NAME).document(favouriteID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Log.i(TAG, "data received");
                        favourite = task.getResult().toObject(Favourite.class);
                        updateUI(favourite);
                    } else {
                        Log.i(TAG, "no data received");
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.internal_error_fav, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUI(Favourite favourite) {
        if (favourite != null && favourite.getDescription() != null)
            edit_text.setText(favourite.getDescription());
    }

    @Override
    public void onClick(View view) {
        if (view == saveBTN)
            saveFavouriteToDB();
    }

    private void saveFavouriteToDB() {
        favourite.setDescription(edit_text.getText().toString());
        firebaseFirestore.collection(Favourite.COLLECTION_NAME).document(favouriteID).set(favourite, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HideSoftKeyboard.hideKeyboard(MoreOnFavourites.this);
                if (task.isSuccessful())
                    customDialogs.showSuccessSnackbar(frameLayout, "All changes were saved successfully!");
                else
                    customDialogs.showErrorSnackbar(frameLayout, task.getException().getMessage());
            }
        });
    }

}