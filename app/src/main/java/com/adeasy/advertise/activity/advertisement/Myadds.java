package com.adeasy.advertise.activity.advertisement;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.firebase.AdvertisementFirebase;
import com.adeasy.advertise.firebase.AdvertisementFirebaseImpl;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.model.Advertisement;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.squareup.picasso.Picasso;

public class Myadds extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdvertisementFirebase advertisementFirebase = new AdvertisementFirebaseImpl();
    SwipeRefreshLayout swipeRefreshLayout;
    FirestorePagingAdapter<Advertisement, ViewHolderListAdds> firestorePagingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadds);

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

        recyclerView = findViewById(R.id.myaddsRecycle);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestorePagingAdapter.refresh();
            }
        });

    }

    public void loadData() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(advertisementFirebase.viewAddsAll(), config, Advertisement.class)
                .build();

        firestorePagingAdapter =
                new FirestorePagingAdapter<Advertisement, ViewHolderListAdds>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderListAdds holder, final int position, @NonNull Advertisement advertisement) {

                        holder.getMyadsTitle().setText(advertisement.getTitle());
                        holder.getMyadsPrice().setText("Rs." + advertisement.getPrice());
                        holder.getMyaddsDate().setText(advertisement.getPreetyTime());
                        Picasso.get().load(advertisement.getImageUrl()).into(holder.getImageView());

                        if (!advertisement.isAvailability()) {
                            holder.getMyadsAprooved().setText("Not Available");
                            holder.getMyadsAprooved().setBackgroundResource(R.color.colorPrimary);
                            holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        } else if (advertisement.isApproved()) {
                            holder.getMyadsAprooved().setText("Live");
                            holder.getMyadsAprooved().setBackgroundResource(R.color.colorSucess);
                            holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        } else {
                            holder.getMyadsAprooved().setText("Not Approved");
                            holder.getMyadsAprooved().setBackgroundResource(R.color.colorDanger);
                            holder.getMyadsAprooved().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
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

                                                                advertisementFirebase.deleteAdd(getItem(position).getId(), (String)getItem(position).get("imageUrl"),Myadds.this);
                                                                firestorePagingAdapter.refresh();

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
                                                Intent intent = new Intent(getApplicationContext(), EditAdvertisement.class);
                                                intent.putExtra("adID", getItem(position).get("id").toString());
                                                intent.putExtra("adCID", getItem(position).get("categoryID").toString());
                                                startActivity(intent);
                                            }
                                        });

                                if ((Boolean)getItem(position).get("availability")) {

                                    alertDialog.setPositiveButton("Hide ad", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            advertisementFirebase.hideAdd(getItem(position).getId(), false, Myadds.this);
                                            firestorePagingAdapter.refresh();
                                        }
                                    });
                                } else {

                                    alertDialog.setPositiveButton("Show ad", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            advertisementFirebase.hideAdd(getItem(position).getId(), true, Myadds.this);
                                            firestorePagingAdapter.refresh();
                                        }
                                    });
                                }

                                alertDialog.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ViewHolderListAdds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row, parent, false);
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
                        refresh();
                    }

                };

        firestorePagingAdapter.startListening();
        recyclerView.setAdapter(firestorePagingAdapter);

    }
}