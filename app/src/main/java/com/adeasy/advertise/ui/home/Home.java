package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.AdvertismentSearchCallback;
import com.adeasy.advertise.helper.ViewHolderAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.search_manager.AdvertismentSearchManager;
import com.adeasy.advertise.ui.advertisement.CategoryPicker;
import com.adeasy.advertise.ui.advertisement.HomeAdSearch;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Home extends Fragment implements AdvertisementCallback, AdvertismentSearchCallback, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Home";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toolbar toolbar;
    TextView adCountText, category_picker;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirestorePagingAdapter<Advertisement, ViewHolderAdds> firestorePagingAdapter;
    AdvertisementManager advertisementManager;
    String searchKey;
    Category category_selected;
    AdvertismentSearchManager advertismentSearchManager;
    private static final String SEARCH_KEY = "search_key";
    private static final String CATEGORY_SELECTED = "category_selected";
    private static final int CATEGORY_PICKER = 4662;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        recyclerView = view.findViewById(R.id.adMenuRecyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setHasFixedSize(false);
        advertisementManager = new AdvertisementManager(this);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        adCountText = toolbar.findViewById(R.id.adResults);
        category_picker = view.findViewById(R.id.category_picker);

        category_picker.setOnClickListener(this);

        advertismentSearchManager = new AdvertismentSearchManager(this);

        try {
            searchKey = getArguments().getString(SEARCH_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            category_selected = (Category) getArguments().getSerializable(CATEGORY_SELECTED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (searchKey != null) {
            toolbar.setTitle(searchKey);
            toolbar.setSubtitle(getString(R.string.loading));
            advertismentSearchManager.searchAdsHome(searchKey);
        } else if (category_selected != null) {
            toolbar.setTitle(category_selected.getName());
            toolbar.setSubtitle(getString(R.string.loading));
            loadData(advertisementManager.viewAddsByCategoryHome(category_selected.getId()));
        } else {
            loadData(advertisementManager.viewAdds());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view == category_picker)
            getActivity().startActivityForResult(new Intent(getActivity(), CategoryPicker.class), CATEGORY_PICKER);
    }

    public void loadData(Query query) {

        advertisementManager.getCount(query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Advertisement.class)
                .build();

        advertisementManager.viewAdds().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<Advertisement> chats = snapshot.toObjects(Advertisement.class);

                // Update UI
                // ...
            }
        });

        firestorePagingAdapter =
                new FirestorePagingAdapter<Advertisement, ViewHolderAdds>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolderAdds holder, final int position, @NonNull Advertisement advertisement) {

                        holder.titleView.setText(advertisement.getTitle());
                        holder.dateView.setText(advertisement.getPreetyTime());
                        holder.priceView.setText(advertisement.getPreetyCurrency());

                        try {
                            Picasso.get().load(advertisement.getImageUrls().get(0)).into(holder.imageView);

                        } catch (Exception e) {

                        }

                        if (advertisement.isBuynow())
                            holder.buyNow.setVisibility(View.VISIBLE);

                        else
                            holder.buyNow.setVisibility(View.GONE);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), com.adeasy.advertise.ui.advertisement.Advertisement.class);
                                intent.putExtra("adID", getItem(position).getId());
                                intent.putExtra("adCID", (String) getItem(position).get("categoryID"));
                                startActivity(intent);
                                //Toast.makeText(view.getContext(), getItem(position).getId(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolderAdds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ad_menu, parent, false);
                        return new ViewHolderAdds(view);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                                mSwipeRefreshLayout.setRefreshing(true);
                            case LOADING_MORE:
                                // Do your loading animation
                                mSwipeRefreshLayout.setRefreshing(true);
                                break;

                            case LOADED:
                                // Stop Animation
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
                                Toast.makeText(getContext(), "No more ads..", Toast.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case ERROR:
                                mSwipeRefreshLayout.setRefreshing(false);
                                retry();
                                break;
                        }
                    }

                    @Override
                    protected void onError(@NonNull Exception e) {
                        super.onError(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        //Handle Error
                        refresh();
                    }

                };

        firestorePagingAdapter.startListening();
        recyclerView.setAdapter(firestorePagingAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestorePagingAdapter.refresh();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firestorePagingAdapter != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    firestorePagingAdapter.refresh();
                }
            });
            firestorePagingAdapter.startListening();
        }
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        if (firestorePagingAdapter != null)
            firestorePagingAdapter.stopListening();
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
        if (task.isSuccessful()) {
            try {
                if (adCountText != null)
                    adCountText.setText(task.getResult().size() + " results");
                if (searchKey != null || category_selected != null)
                    toolbar.setSubtitle(task.getResult().size() + " results");
            } catch (NullPointerException e) {
                Log.i(TAG, "fragments changed");
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        advertisementManager.destroy();
    }

    @Override
    public void onSearchComplete(List<String> ids, List<Advertisement> advertisementList) {
        loadData(advertisementManager.viewAddsSearch(ids));
    }

}