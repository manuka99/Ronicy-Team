package com.adeasy.advertise.ui.favaourite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Favourite;
import com.adeasy.advertise.ui.home.NoData;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFavourites extends AppCompatActivity {

    RecyclerView myFavs;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter adapter;
    CustomDialogs customDialogs;
    FrameLayout frameLayout;
    Context context;
    AdvertisementManager advertisementManager;
    List<String> adIds;
    Map<String, String> adIdsFIds;
    Toolbar toolbar;


    private static final String TAG = "MyFavourites";
    private static final String FAVOURITE_COLLECTION = "Favourites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divya_activity_my_favourites);

        context = this;
        myFavs = findViewById(R.id.myFavs);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.frameLayout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        customDialogs = new CustomDialogs(this);
        advertisementManager = new AdvertisementManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myFavs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (firebaseAuth.getCurrentUser() != null) {
            StartFavourites();
        }
    }

    private void StartFavourites() {
        firebaseFirestore.collection(FAVOURITE_COLLECTION).whereEqualTo("userID", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                getSupportActionBar().setSubtitle( task.getResult().size() + " results");
                adIds = new ArrayList<>();
                adIdsFIds = new HashMap<String, String>();
                if(task.isSuccessful()){
                for (Favourite favourite : task.getResult().toObjects(Favourite.class)) {
                    adIds.add(favourite.getAdvertisementID());
                    adIdsFIds.put(favourite.getAdvertisementID(), favourite.getFavouriteID());
                }
                if (adIds.size() > 0) {
                    loadMyFavourites(adIds);
                    frameLayout.setVisibility(View.GONE);
                } else {
                    getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new NoData()).commit();
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }}
        });
    }

    private void loadMyFavourites(final List<String> adIds) {
        FirestoreRecyclerOptions<Advertisement> options =
                new FirestoreRecyclerOptions.Builder<com.adeasy.advertise.model.Advertisement>()
                        .setQuery(advertisementManager.viewAddsHome(adIds, null, null, false), com.adeasy.advertise.model.Advertisement.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Advertisement, ViewHolderListAdds>(options) {
            @Override
            public void onBindViewHolder(ViewHolderListAdds holder, final int position, final com.adeasy.advertise.model.Advertisement advertisement) {
                try {
                    holder.getMyadsTitle().setText(advertisement.getTitle());
                    holder.getMyadsPrice().setText(advertisement.getPreetyCurrency());
                    holder.getMyaddsDate().setText(advertisement.getPreetyTime());
                    Picasso.get().load(advertisement.getImageUrls().get(0)).fit().into(holder.getImageView());
                    holder.getMyadsAprooved().setVisibility(View.GONE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), com.adeasy.advertise.ui.advertisement.Advertisement.class);
                            intent.putExtra("adID", getItem(position).getId());
                            intent.putExtra("adCID", (String) getItem(position).getCategoryID());
                            startActivity(intent);
                            //Toast.makeText(view.getContext(), getItem(position).getId(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Manage favourite")
                                    .setMessage("Users can now update and manage favourites, click to view more...")
                                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new AlertDialog.Builder(context)

                                                    .setMessage("Are you sure you want to delete?")

                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            try {
                                                                firebaseFirestore.collection(FAVOURITE_COLLECTION).document(adIdsFIds.get(advertisement.getId())).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            StartFavourites();
                                                                            Toast.makeText(context, "Success: Advertisement was removed", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        if (!task.isSuccessful()) {
                                                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    })
                                                    .show();
                                        }
                                    })

                                    .setNegativeButton("More", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })

                                    .show();
                            return true;
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public ViewHolderListAdds onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.manuka_ads_row, group, false);

                return new ViewHolderListAdds(view);
            }

            @Override
            public void onDataChanged() {

                //if (getSnapshots().size() == 0)

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
                e.printStackTrace();
            }

        };

        adapter.startListening();
        myFavs.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

}