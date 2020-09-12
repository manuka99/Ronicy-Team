package com.adeasy.advertise.ui.administration.advertisement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.advertisement.Myadds;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class NewAds extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AdvertisementManager advertisementManager;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CustomDialogs customDialogs;

    public NewAds() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAds.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAds newInstance(String param1, String param2) {
        NewAds fragment = new NewAds();
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
        View view = inflater.inflate(R.layout.manuka_admin_fragment_new_ads, container, false);
        recyclerView = view.findViewById(R.id.myaddsRecycle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMyadds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        advertisementManager = new AdvertisementManager();
        firebaseAuth = FirebaseAuth.getInstance();
        customDialogs = new CustomDialogs(getActivity());
        loadData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.onDataChanged();
            }
        });
    }

    public void loadData() {
        FirestoreRecyclerOptions<Advertisement> options =
                new FirestoreRecyclerOptions.Builder<Advertisement>()
                        .setQuery(advertisementManager.viewNotReviewedAdds(), Advertisement.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Advertisement, ViewHolderListAdds>(options) {
            @Override
            public void onBindViewHolder(ViewHolderListAdds holder, final int position, Advertisement advertisement) {
                try {
                    holder.getMyadsTitle().setText(advertisement.getTitle());
                    holder.getMyadsPrice().setText("Rs " + advertisement.getPrice());
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
                                    .setTitle("Ad - " + getItem(position).getTitle())
                                    .setMessage("Note any changes made cannot be revert. Select below and proceed.")
                                    .setNeutralButton("More", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), MoreActionsOnAd.class);
                                            intent.putExtra("adID", getItem(position).getId());
                                            intent.putExtra("adCID", getItem(position).getCategoryID());
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Edit ad", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), EditAd.class);
                                            intent.putExtra("adID", getItem(position).getId());
                                            intent.putExtra("adCID", getItem(position).getCategoryID());
                                            startActivity(intent);
                                        }
                                    });

                            alertDialog.setPositiveButton("View ad", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getContext(), com.adeasy.advertise.ui.advertisement.Advertisement.class);
                                    intent.putExtra("adID", getItem(position).getId());
                                    intent.putExtra("adCID", getItem(position).getCategoryID());
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

            @Override
            public ViewHolderListAdds onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.manuka_ads_row, group, false);

                return new ViewHolderListAdds(view);
            }

            @Override
            public void onDataChanged() {
                swipeRefreshLayout.setRefreshing(true);
                // Called each time there is a new query snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                if (getSnapshots().size() == getItemCount())
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
                e.printStackTrace();
                customDialogs.showPermissionDeniedStorage();
            }


        };
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

}