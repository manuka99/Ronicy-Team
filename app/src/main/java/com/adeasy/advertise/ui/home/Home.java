package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.RecyclerAdapterPublicFeed;
import com.adeasy.advertise.adapter.RecyclerAdapterPublicFeedHorizontal;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.AdvertismentSearchCallback;
import com.adeasy.advertise.helper.EndlessScrollListener;
import com.adeasy.advertise.helper.ViewHolderAdds;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.search_manager.AdvertismentSearchManager;
import com.adeasy.advertise.ui.advertisement.CategoryPicker;
import com.adeasy.advertise.ui.advertisement.FilterSearchResult;
import com.adeasy.advertise.ui.advertisement.HomeAdSearch;
import com.adeasy.advertise.ui.advertisement.LocationPicker;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

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
    TextView adCountText, category_picker, location_picker;
    RecyclerView recyclerView, adMenuRecyclerHorizontalView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdvertisementManager advertisementManager;
    String searchKey, location_selected;
    Category category_selected;
    AdvertismentSearchManager advertismentSearchManager;
    Switch aSwitch;
    List<String> search_ids;
    ImageView filters;
    FrameLayout frameLayout;
    CustomDialogs customDialogs;

    private static final String SEARCH_KEY = "search_key";
    private static final String CATEGORY_SELECTED = "category_selected";
    private static final String LOCATION_SELECTED = "location_selected";
    private static final int LOCATION_PICKER = 6512;
    private static final int CATEGORY_PICKER = 4662;
    private static final int FILTER_PICKER = 8825;

    public static final int ITEM_PER_AD = 6;

    Query query;
    DocumentSnapshot lastDoc;
    List<Object> objectList = new ArrayList<>();
    RecyclerAdapterPublicFeed recyclerAdapterPublicFeed;
    RecyclerAdapterPublicFeedHorizontal recyclerAdapterPublicFeedHorizontal;
    Query finalNewQuery;
    CardView cardViewHeader;
    boolean isHeaderHidden = false;

    Float imageRatio = 0.8f;

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
        mSwipeRefreshLayout.setProgressViewOffset(false,
                getResources().getDimensionPixelSize(R.dimen.refresher_offset),
                getResources().getDimensionPixelSize(R.dimen.refresher_offset_end));
        cardViewHeader = view.findViewById(R.id.cardViewHeader);
        recyclerView = view.findViewById(R.id.adMenuRecyclerView);
        adMenuRecyclerHorizontalView = view.findViewById(R.id.adMenuRecyclerHorizontalView);
        aSwitch = view.findViewById(R.id.switchView);
        recyclerView.setNestedScrollingEnabled(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adMenuRecyclerHorizontalView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        //recyclerView.setHasFixedSize(false);

        customDialogs = new CustomDialogs(getActivity());

        advertisementManager = new AdvertisementManager(this);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        adCountText = toolbar.findViewById(R.id.adResults);
        frameLayout = view.findViewById(R.id.frameLayout);
        category_picker = view.findViewById(R.id.category_picker);
        location_picker = view.findViewById(R.id.location_picker);
        filters = view.findViewById(R.id.filters);
        category_picker.setOnClickListener(this);
        location_picker.setOnClickListener(this);
        filters.setOnClickListener(this);
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

        try {
            location_selected = getArguments().getString(LOCATION_SELECTED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar.setSubtitle(getString(R.string.loading));

        if (category_selected != null) {
            toolbar.setTitle(category_selected.getName());
            category_picker.setText(category_selected.getName());
        }
        if (location_selected != null)
            location_picker.setText(location_selected);

        if (searchKey != null) {
            toolbar.setTitle(searchKey);
            advertismentSearchManager.searchAdsHome(searchKey);
        }

        query = advertisementManager.viewAddsHome(search_ids, category_selected, location_selected, aSwitch.isChecked());

        //new method
        recyclerAdapterPublicFeed = new RecyclerAdapterPublicFeed(getActivity());
        recyclerAdapterPublicFeedHorizontal = new RecyclerAdapterPublicFeedHorizontal(getActivity());

        recyclerView.setAdapter(recyclerAdapterPublicFeed);
        adMenuRecyclerHorizontalView.setAdapter(recyclerAdapterPublicFeedHorizontal);

        loadData2();
        loadDataTopAds();

        mSwipeRefreshLayout.setRefreshing(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aSwitch.setOnClickListener(this);

        NestedScrollView nestedSV = (NestedScrollView) view.findViewById(R.id.nestedScroll);

        if (nestedSV != null) {

            nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > 0) {
                        // Scrolling up
                        if (isHeaderHidden) {
//                            Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.moveup);
//                            cardViewHeader.startAnimation(aniSlide);
//                            cardViewHeader.setVisibility(View.GONE);
                        }

                    } else {
                        // User scrolls down
                        if (!isHeaderHidden) {
//                            Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.movedown);
//                            cardViewHeader.startAnimation(aniSlide);
//                            cardViewHeader.setVisibility(View.VISIBLE);
                        }
                    }

                    if (scrollY > oldScrollY) {
                        if (!isHeaderHidden) {
                            Log.i(TAG, "Scroll DOWN");
                            Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.moveup);
                            cardViewHeader.startAnimation(aniSlide);
                            cardViewHeader.setVisibility(View.INVISIBLE);
                            isHeaderHidden = true;
                        }
                    }
                    if (scrollY < oldScrollY) {
                        if (isHeaderHidden) {
                            Log.i(TAG, "Scroll UP");
                            Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.movedown);
                            cardViewHeader.startAnimation(aniSlide);
                            cardViewHeader.setVisibility(View.VISIBLE);
                            isHeaderHidden = false;
                        }
                    }

                    if (scrollY == 0) {
                        //Log.i(TAG, "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        //Log.i(TAG, "BOTTOM SCROLL");
                        mSwipeRefreshLayout.setRefreshing(true);
                        loadData2();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == category_picker)
            getActivity().startActivityForResult(new Intent(getActivity(), CategoryPicker.class), CATEGORY_PICKER);
        else if (view == location_picker)
            getActivity().startActivityForResult(new Intent(getActivity(), LocationPicker.class), LOCATION_PICKER);
        else if (view == filters)
            getActivity().startActivityForResult(new Intent(getActivity(), FilterSearchResult.class), FILTER_PICKER);
    }

    private void loadData2() {
        finalNewQuery = query.limit(ITEM_PER_AD);

        if (lastDoc == null)
            advertisementManager.getCount(query);
        else
            finalNewQuery = finalNewQuery.startAfter(lastDoc);

        finalNewQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (task.isSuccessful() && finalNewQuery != null && task.getResult().getQuery().equals(finalNewQuery)) {
                    QuerySnapshot documentSnapshots = task.getResult();
                    if (documentSnapshots.size() > 0) {
                        lastDoc = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        objectList.addAll(task.getResult().toObjects(Advertisement.class));
                        recyclerAdapterPublicFeed.setObjects(objectList);
                        recyclerAdapterPublicFeed.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void loadDataTopAds() {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && query != null && task.getResult().getQuery().equals(query)) {
                    QuerySnapshot documentSnapshots = task.getResult();
                    if (documentSnapshots.size() > 0) {
                        objectList.addAll(task.getResult().toObjects(Advertisement.class));
                        recyclerAdapterPublicFeedHorizontal.setObjects(objectList);
                        recyclerAdapterPublicFeedHorizontal.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!new InternetValidation().validateInternet(getActivity()))
            customDialogs.showNoInternetDialog();
        else {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    resetAdapterAndView();
                }
            });
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSwipeRefreshLayout.setRefreshing(true);
                query = advertisementManager.viewAddsHome(search_ids, category_selected, location_selected, b);
                resetAdapterAndView();
            }
        });
    }

    private void resetAdapterAndView() {
        if (recyclerAdapterPublicFeed != null) {
            try {
                adCountText.setText(" loading..");
            } catch (NullPointerException e) {
                Log.i(TAG, "fragments changed");
            }
            try {
                toolbar.setSubtitle("loading..");
            } catch (NullPointerException e) {
                Log.i(TAG, "fragments changed");
            }

            lastDoc = null;
            recyclerAdapterPublicFeed.resetObjects();
            loadData2();
        }
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
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
        if (task.isSuccessful() && query != null && task.getResult().getQuery().equals(query)) {
            try {
                adCountText.setText(task.getResult().size() + " results");
            } catch (NullPointerException e) {
                Log.i(TAG, "fragments changed");
            }
            try {
                toolbar.setSubtitle(task.getResult().size() + " results");
            } catch (NullPointerException e) {
                Log.i(TAG, "fragments changed");
            }

            if (task.getResult().size() == 0) {
                getFragmentManager().beginTransaction().replace(frameLayout.getId(), new NoData()).commit();
                frameLayout.setVisibility(View.VISIBLE);
            } else
                frameLayout.setVisibility(View.GONE);
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
        search_ids = ids;
        Log.i(TAG, "ids: " + ids);
        query = advertisementManager.viewAddsHome(ids, category_selected, location_selected, aSwitch.isChecked());
        resetAdapterAndView();
    }

}