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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PendingRejectedAds extends AppCompatActivity implements AdvertisementCallback {

    private static final String TAG = "PendingRejectedAds";
    public static final String ADS_TYPE = "ads_type";
    public static final String NOT_REVIEWED = "PendingAds";
    public static final String REJECTED = "RejectedAds";

    Query query;
    SwipeRefreshLayout swipeRefreshLayout;
    FirestorePagingAdapter<Advertisement, ViewHolderListAdds> firestorePagingAdapter;
    AdvertisementManager advertisementManager;
    RecyclerView recyclerView;
    Toolbar toolbar;
    CustomDialogs customDialogs;
    List<String> imageUrls;
    FrameLayout frameLayout;
    boolean isRejectedAds = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_pending_rejected_ads);

        frameLayout = findViewById(R.id.frameLayout);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshMyadds);
        recyclerView = findViewById(R.id.myaddsRecycle);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        customDialogs = new CustomDialogs(this);
        advertisementManager = new AdvertisementManager(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (getIntent().hasExtra(ADS_TYPE)) {
                if (getIntent().getStringExtra(ADS_TYPE).equals(NOT_REVIEWED)) {
                    isRejectedAds = false;
                    query = advertisementManager.getMyReviewingAds(FirebaseAuth.getInstance().getUid());
                    getSupportActionBar().setTitle(R.string.ads_pending_review);
                } else if (getIntent().getStringExtra(ADS_TYPE).equals(REJECTED)) {
                    isRejectedAds = true;
                    query = advertisementManager.getMyRejectedAds(FirebaseAuth.getInstance().getUid());
                    getSupportActionBar().setTitle(R.string.ads_pending_editing);

                } else
                    finish();

                loadData();
            }
        } else
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firestorePagingAdapter != null)
            firestorePagingAdapter.refresh();
    }

    private void loadData() {

        advertisementManager.getCount(query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(6)
                .build();

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Advertisement.class)
                .build();

        firestorePagingAdapter =
                new FirestorePagingAdapter<Advertisement, ViewHolderListAdds>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderListAdds holder, final int position, @NonNull final Advertisement advertisement) {
                        try {
                            holder.getMyadsTitle().setText(advertisement.getTitle());
                            holder.getMyadsPrice().setText(advertisement.getPreetyCurrency());
                            holder.getMyaddsDate().setText(advertisement.getPreetyTime());
                            Picasso.get().load(advertisement.getImageUrls().get(0)).fit().into(holder.getImageView());

                            if (isRejectedAds) {
                                holder.getMyadsAprooved().setText("Not Approved");
                                holder.getMyadsAprooved().setBackgroundResource(R.color.colorDanger);
                                holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                                if (advertisement.getUnapprovedReason() != null) {
                                    holder.getMyaddsUaprovedReason().setText(advertisement.getUnapprovedReason());
                                    holder.getMyaddsUaprovedReason().setVisibility(View.VISIBLE);
                                }
                            } else {
                                holder.getMyadsAprooved().setText("Not Yet Reviewed");
                                holder.getMyadsAprooved().setBackgroundResource(R.color.colorPrimary);
                                holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                                holder.getMyaddsUaprovedReason().setVisibility(View.GONE);
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isRejectedAds) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingRejectedAds.this)

                                                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

                                                .setTitle("My ad - " + advertisement.getTitle())

                                                .setMessage("Note any changes made cannot be revert. Select below and proceed.")

                                                .setNegativeButton("Delete ad", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {


                                                        new AlertDialog.Builder(PendingRejectedAds.this)

                                                                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

                                                                .setTitle("Are you sure you want to delete your advertisement - " + advertisement.getTitle())

                                                                .setMessage("Note any changes made cannot be revert.")

                                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        imageUrls = advertisement.getImageUrls();
                                                                        advertisementManager.moveAdToTrash(advertisement);
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

                                                .setPositiveButton("Edit ad", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(getApplicationContext(), EditAd.class);
                                                        intent.putExtra("adID", advertisement.getId());
                                                        intent.putExtra("adCID", advertisement.getCategoryID());
                                                        startActivity(intent);
                                                    }
                                                });

                                        alertDialog.show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @NonNull
                    @Override
                    public ViewHolderListAdds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        if (isRejectedAds) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_row_card, parent, false);
                            return new ViewHolderListAdds(view);
                        } else {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_row_card_disable, parent, false);
                            return new ViewHolderListAdds(view);
                        }
                    }

                    @Override
                    public void refresh() {
                        super.refresh();
                        advertisementManager.getCount(query);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                                swipeRefreshLayout.setRefreshing(true);
                            case LOADING_MORE:
                                // Do your loading animation
                                swipeRefreshLayout.setRefreshing(true);
                                break;

                            case LOADED:
                                // Stop Animation
                                swipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
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
                            customDialogs.showPermissionDeniedStorage();
                        }
                        ;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        return super.getItemViewType(position);
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
            Toast.makeText(getApplicationContext(), "Success: All changes were saved", Toast.LENGTH_LONG).show();
            firestorePagingAdapter.refresh();
        } else if (task != null) {
            Toast.makeText(getApplicationContext(), "Error, " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                firestorePagingAdapter.stopListening();
                customDialogs.showPermissionDeniedStorage();
            }
        }

        if (firestorePagingAdapter != null)
            firestorePagingAdapter.refresh();
    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {
        if (task != null && task.isSuccessful()) {
            if (imageUrls != null) {
                advertisementManager.deleteMultipleImages(imageUrls);
                imageUrls = null;
            }
            firestorePagingAdapter.refresh();
            Toast.makeText(getApplicationContext(), "Success: Your advertisement was deleted", Toast.LENGTH_LONG).show();
        } else if (task != null) {
            Toast.makeText(getApplicationContext(), "Error: Your advertisement was not deleted", Toast.LENGTH_LONG).show();
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                firestorePagingAdapter.stopListening();
                customDialogs.showPermissionDeniedStorage();
            }
        }

        if (firestorePagingAdapter != null)
            firestorePagingAdapter.refresh();
    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {
        if (task != null && task.isSuccessful()) {

            Query taskQuery = task.getResult().getQuery();
            int resultCount = task.getResult().size();

            if (taskQuery.equals(query)) {
                if (resultCount == 0) {
                    getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new NoData()).commit();
                    getSupportActionBar().setSubtitle("No results");
                    frameLayout.setVisibility(View.VISIBLE);
                } else {
                    frameLayout.setVisibility(View.GONE);
                    getSupportActionBar().setSubtitle("Total ads: " + task.getResult().size());
                }
            }
        }
    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

}