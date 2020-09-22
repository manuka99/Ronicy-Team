package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.ui.home.NoData;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Myadds extends AppCompatActivity implements AdvertisementCallback {

    Context context;
    List<String> imageUrls;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdvertisementManager advertisementManager;
    SwipeRefreshLayout swipeRefreshLayout;
    FirestorePagingAdapter<Advertisement, ViewHolderListAdds> firestorePagingAdapter;
    FirebaseAuth firebaseAuth;
    CustomDialogs customErrorDialogs;
    FrameLayout frameLayout;
    private static final String TAG = "Myadds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_myadds);

        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My ads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshMyadds);

        frameLayout = findViewById(R.id.frameLayout);
        recyclerView = findViewById(R.id.myaddsRecycle);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        advertisementManager = new AdvertisementManager(this);
        customErrorDialogs = new CustomDialogs(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)
            loadData();

    }

    public void loadData() {

        advertisementManager.getCount(advertisementManager.viewMyAddsAll());

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(advertisementManager.viewMyAddsAll(), config, Advertisement.class)
                .build();

        firestorePagingAdapter =
                new FirestorePagingAdapter<Advertisement, ViewHolderListAdds>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderListAdds holder, final int position, @NonNull Advertisement advertisement) {
                        try {
                            holder.getMyadsTitle().setText(advertisement.getTitle());
                            holder.getMyadsPrice().setText(advertisement.getPreetyCurrency());
                            holder.getMyaddsDate().setText(advertisement.getPreetyTime());
                            Picasso.get().load(advertisement.getImageUrls().get(0)).fit().into(holder.getImageView());

                            if (!advertisement.isAvailability()) {
                                holder.getMyadsAprooved().setText("Not Available");
                                holder.getMyadsAprooved().setBackgroundResource(R.color.colorPrimary);
                                holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                                holder.getMyaddsUaprovedReason().setVisibility(View.GONE);
                            } else if (advertisement.isApproved()) {
                                holder.getMyadsAprooved().setText("Live");
                                holder.getMyadsAprooved().setBackgroundResource(R.color.colorSucess);
                                holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                                holder.getMyaddsUaprovedReason().setVisibility(View.GONE);
                            } else {
                                holder.getMyadsAprooved().setText("Not Approved");
                                holder.getMyadsAprooved().setBackgroundResource(R.color.colorDanger);
                                holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                                if (advertisement.getUnapprovedReason() != null) {
                                    holder.getMyaddsUaprovedReason().setText(advertisement.getUnapprovedReason());
                                    holder.getMyaddsUaprovedReason().setVisibility(View.VISIBLE);
                                } else
                                    holder.getMyaddsUaprovedReason().setVisibility(View.GONE);
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myadds.this)

                                            .setIcon(android.R.drawable.ic_dialog_alert)

                                            .setTitle("My ad - " + getItem(position).get("title"))

                                            .setMessage("Note any changes made cannot be revert. Select below and proceed.")

                                            .setNeutralButton("Delete ad", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    AlertDialog alertDialog = new AlertDialog.Builder(Myadds.this)

                                                            .setIcon(android.R.drawable.ic_dialog_alert)

                                                            .setTitle("Are you sure you want to delete your advertisement - " + getItem(position).get("title"))

                                                            .setMessage("Note any changes made cannot be revert.")

                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    imageUrls = (List<String>) getItem(position).get("imageUrls");
                                                                    advertisementManager.moveAdToTrash(getItem(position).toObject(Advertisement.class));
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

                                            .setNegativeButton("Edit ad", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(getApplicationContext(), EditAd.class);
                                                    intent.putExtra("adID", getItem(position).get("id").toString());
                                                    intent.putExtra("adCID", getItem(position).get("categoryID").toString());
                                                    startActivity(intent);
                                                }
                                            });

                                    if ((Boolean) getItem(position).get("availability")) {

                                        alertDialog.setPositiveButton("Hide ad", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                advertisementManager.hideAdd(getItem(position).getId(), false);
                                            }
                                        });
                                    } else {

                                        alertDialog.setPositiveButton("Show ad", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                advertisementManager.hideAdd(getItem(position).getId(), true);
                                            }
                                        });
                                    }

                                    alertDialog.show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @NonNull
                    @Override
                    public ViewHolderListAdds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_row, parent, false);
                        return new ViewHolderListAdds(view);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                            case LOADING_MORE:
                                // Do your loading animation
                                swipeRefreshLayout.setRefreshing(true);
                                break;

                            case LOADED:
                                // Stop Animation
                                swipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
                                Toast.makeText(getApplicationContext(), "last..", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                                break;

                            case ERROR:
                                retry();
                                break;
                        }
                    }

                    @Override
                    protected void onError(@NonNull Exception e) {
                        super.onError(e);
                        swipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        //Handle Error
                        Log.d(TAG, "onError: " + e.getClass());
                        if (e instanceof FirebaseFirestoreException) {
                            ((FirebaseFirestoreException) e).getCode().equals(PERMISSION_DENIED);
                            firestorePagingAdapter.stopListening();
                            customErrorDialogs.showPermissionDeniedStorage();
                        }
                        ;
                    }

                };

        firestorePagingAdapter.startListening();
        recyclerView.setAdapter(firestorePagingAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestorePagingAdapter.refresh();
            }
        });
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onCompleteInsertAd(Task<Void> task) {
        if (task != null && task.isSuccessful()) {
            Toast.makeText(context, "Success: All changes were saved", Toast.LENGTH_LONG).show();
            firestorePagingAdapter.refresh();
        } else if (task != null) {
            Toast.makeText(context, "Error, " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                firestorePagingAdapter.stopListening();
                customErrorDialogs.showPermissionDeniedStorage();
            }

        }
    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {
        if (task != null && task.isSuccessful()) {
            if (imageUrls != null) {
                advertisementManager.deleteMultipleImages(imageUrls);
                imageUrls = null;
            }
            firestorePagingAdapter.refresh();
            Toast.makeText(context, "Success: Your advertisement was deleted", Toast.LENGTH_LONG).show();
        } else if (task != null) {
            Toast.makeText(context, "Error: Your advertisement was not deleted", Toast.LENGTH_LONG).show();
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                firestorePagingAdapter.stopListening();
                customErrorDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            if (task.getResult().size() == 0) {
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new NoData()).commit();
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                frameLayout.setVisibility(View.GONE);
                getSupportActionBar().setSubtitle("Total ads: " + task.getResult().size());
            }
        }
    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }

}