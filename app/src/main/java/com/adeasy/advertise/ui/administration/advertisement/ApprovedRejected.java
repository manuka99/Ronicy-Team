package com.adeasy.advertise.ui.administration.advertisement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ApprovedRejected extends Fragment implements View.OnClickListener, AdvertisementCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AdvertisementManager advertisementManager;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirestorePagingAdapter<Advertisement, ViewHolderListAdds> firestorePagingAdapter;
    FirestorePagingOptions<Advertisement> options;
    Button approved, rejected;
    Query query;

    CardView noDataFragment;

    public ApprovedRejected() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApprovedRejected.
     */
    // TODO: Rename and change types and number of parameters
    public static ApprovedRejected newInstance(String param1, String param2) {
        ApprovedRejected fragment = new ApprovedRejected();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.manuka_admin_fragment_approved_rejected, container, false);
        noDataFragment = view.findViewById(R.id.noDataFragment);
        recyclerView = view.findViewById(R.id.myaddsRecycle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMyadds);

        approved = view.findViewById(R.id.approved);
        rejected = view.findViewById(R.id.rejected);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        advertisementManager = new AdvertisementManager(this);
        firebaseAuth = FirebaseAuth.getInstance();

        approved.setOnClickListener(this);
        rejected.setOnClickListener(this);

        onApproveAdsUi();
        return view;
    }

    public void loadData() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(3)
                .build();

        options = new FirestorePagingOptions.Builder<Advertisement>()
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
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                            .setIcon(R.drawable.ic_baseline_info_24_red)
                                            .setTitle("Ad - " + getItem(position).get("title"))
                                            .setMessage("Note any changes made cannot be revert. Select below and proceed.")
                                            .setNeutralButton("More", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(getApplicationContext(), MoreActionsOnAd.class);
                                                    intent.putExtra("adID", advertisement.getId());
                                                    intent.putExtra("adCID", advertisement.getCategoryID());
                                                    startActivity(intent);
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

                                    alertDialog.setPositiveButton("View ad", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getContext(), com.adeasy.advertise.ui.advertisement.Advertisement.class);
                                            intent.putExtra("adID", advertisement.getId());
                                            intent.putExtra("adCID", advertisement.getCategoryID());
                                            startActivity(intent);
                                        }
                                    });
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
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_row_card, parent, false);
                        return new ViewHolderListAdds(view);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                                advertisementManager.getCount(query);
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
                                Toast.makeText(getApplicationContext(), "last..fg", Toast.LENGTH_SHORT).show();
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestorePagingAdapter.refresh();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == approved) {
            onApproveAdsUi();
        } else if (view == rejected) {
            onRejectedAdsUi();
        }
    }

    private void onApproveAdsUi() {
        approved.setBackgroundResource(R.drawable.grey_btn_half_left_round);
        rejected.setBackgroundResource(R.drawable.red_btn_half_round);
        query = advertisementManager.viewApprovedAdds();
        loadData();
    }

    private void onRejectedAdsUi() {
        rejected.setBackgroundResource(R.drawable.grey_btn_half_right_round);
        approved.setBackgroundResource(R.drawable.blue_btn_half_round);
        query = advertisementManager.viewRejectedAdds();
        loadData();
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onCompleteInsertAd(Task<Void> task) {

    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {
        if (task != null && task.isSuccessful() && task.getResult().size() > 0) {
            noDataFragment.setVisibility(View.GONE);
        } else {
            noDataFragment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

}