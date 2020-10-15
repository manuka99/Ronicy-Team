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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.athentication.LoginRegister;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.ui.home.NoData;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
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
public class Myadds extends AppCompatActivity implements AdvertisementCallback, View.OnClickListener {

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
    CustomDialogs customDialogs;
    ProgressBar progressBarMain, progressBarRecycler;
    private static final String TAG = "Myadds";

    LinearLayout rejectedAdsLayout, notReviewedAdsLayout, publishedAdsLayout;
    TextView publishedAdsCount, reviewingAdsCount, rejectedAdsCount;
    Query myAllAdsQuery, myReviewingAdsQuery, myRejectedAdsQuery, myPublishedAdsQuery;

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

        progressBarMain = findViewById(R.id.progressBarMain);
        progressBarRecycler = findViewById(R.id.progressBarRecycler);
        rejectedAdsLayout = findViewById(R.id.rejectedAds);
        notReviewedAdsLayout = findViewById(R.id.notReviewedAds);
        publishedAdsLayout = findViewById(R.id.publishedAds);

        rejectedAdsCount = findViewById(R.id.rejectedAdsCountView);
        reviewingAdsCount = findViewById(R.id.reviewingAdsCountView);
        publishedAdsCount = findViewById(R.id.publishedAdsCountView);

        //listeners
        rejectedAdsLayout.setOnClickListener(this);
        notReviewedAdsLayout.setOnClickListener(this);

        recyclerView = findViewById(R.id.myaddsRecycle);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        customDialogs = new CustomDialogs(this);

        advertisementManager = new AdvertisementManager(this);
        customErrorDialogs = new CustomDialogs(this);

        if (firebaseAuth.getCurrentUser() == null)
            getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new LoginRegister()).commit();
        else {
            //init queries
            String uid = firebaseAuth.getCurrentUser().getUid();
            myAllAdsQuery = advertisementManager.viewMyAllAds(uid);
            myPublishedAdsQuery = advertisementManager.viewMyPublishedAddsAll(uid);
            myReviewingAdsQuery = advertisementManager.getMyReviewingAds(uid);
            myRejectedAdsQuery = advertisementManager.getMyRejectedAds(uid);

            //load ads count
            advertisementManager.getCount(myPublishedAdsQuery);
            advertisementManager.getCount(myReviewingAdsQuery);
            advertisementManager.getCount(myRejectedAdsQuery);

            loadPublishedAds();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!new InternetValidation().validateInternet(getApplicationContext()))
            customDialogs.showNoInternetDialog();
    }

    public void loadPublishedAds() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(6)
                .build();

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(myPublishedAdsQuery, config, Advertisement.class)
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

                            holder.getMyaddsUaprovedReason().setVisibility(View.GONE);

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
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myadds.this)

                                            .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

                                            .setTitle("My ad - " + advertisement.getTitle())

                                            .setMessage("Note any changes made cannot be revert. Select below and proceed.")

                                            .setNeutralButton("Delete ad", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    new AlertDialog.Builder(Myadds.this)

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

                                            .setNegativeButton("Edit ad", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(getApplicationContext(), EditAd.class);
                                                    intent.putExtra("adID", advertisement.getId());
                                                    intent.putExtra("adCID", advertisement.getCategoryID());
                                                    startActivity(intent);
                                                }
                                            });

                                    if (advertisement.isAvailability()) {

                                        alertDialog.setPositiveButton("Hide ad", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                advertisementManager.hideAdd(advertisement.getId(), false);
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
                    public void refresh() {
                        super.refresh();
                        initView();
                        swipeRefreshLayout.setRefreshing(false);
                        advertisementManager.getCount(myPublishedAdsQuery);
                        advertisementManager.getCount(myReviewingAdsQuery);
                        advertisementManager.getCount(myRejectedAdsQuery);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                                swipeRefreshLayout.setRefreshing(false);
                            case LOADING_MORE:
                                // Do your loading animation
                                swipeRefreshLayout.setRefreshing(false);
                                break;

                            case LOADED:
                                // Stop Animation
                                swipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
                                swipeRefreshLayout.setRefreshing(false);
                                progressBarRecycler.setVisibility(View.GONE);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == notReviewedAdsLayout) {
            Intent intent = new Intent(getApplication(), PendingRejectedAds.class);
            intent.putExtra(PendingRejectedAds.ADS_TYPE, PendingRejectedAds.NOT_REVIEWED);
            startActivity(intent);
        } else if (view == rejectedAdsLayout) {
            Intent intent = new Intent(getApplication(), PendingRejectedAds.class);
            intent.putExtra(PendingRejectedAds.ADS_TYPE, PendingRejectedAds.REJECTED);
            startActivity(intent);
        }
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
            Toast.makeText(context, "Success: Your advertisement was deleted", Toast.LENGTH_LONG).show();
        } else if (task != null) {
            Toast.makeText(context, "Error: Your advertisement was not deleted", Toast.LENGTH_LONG).show();
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                firestorePagingAdapter.stopListening();
                customErrorDialogs.showPermissionDeniedStorage();
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

            if (taskQuery.equals(myAllAdsQuery)) {
                if (resultCount == 0) {
                    getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new NoData()).commit();
                    getSupportActionBar().setSubtitle("No ads Posted ");
                    frameLayout.setVisibility(View.VISIBLE);
                } else {
                    frameLayout.setVisibility(View.GONE);
                    getSupportActionBar().setSubtitle("Total ads: " + task.getResult().size());
                }
            } else if (taskQuery.equals(myPublishedAdsQuery)) {
                if (resultCount == 0) {
                    publishedAdsLayout.setVisibility(View.GONE);
                } else {
                    publishedAdsLayout.setVisibility(View.VISIBLE);
                    publishedAdsCount.setText(String.valueOf(resultCount));
                }
                progressBarMain.setVisibility(View.GONE);
            } else if (taskQuery.equals(myRejectedAdsQuery)) {
                if (resultCount == 0) {
                    rejectedAdsLayout.setVisibility(View.GONE);
                } else {
                    rejectedAdsLayout.setVisibility(View.VISIBLE);
                    rejectedAdsCount.setText(String.valueOf(resultCount));
                }
            } else if (taskQuery.equals(myReviewingAdsQuery)) {
                if (resultCount == 0) {
                    notReviewedAdsLayout.setVisibility(View.GONE);
                } else {
                    notReviewedAdsLayout.setVisibility(View.VISIBLE);
                    reviewingAdsCount.setText(String.valueOf(resultCount));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementManager.destroy();
    }

    private void initView() {
        progressBarMain.setVisibility(View.VISIBLE);
        progressBarRecycler.setVisibility(View.VISIBLE);
        publishedAdsLayout.setVisibility(View.GONE);
        notReviewedAdsLayout.setVisibility(View.GONE);
        rejectedAdsLayout.setVisibility(View.GONE);
    }

}