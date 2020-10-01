package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.HomeViewModel;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.helper.ViewHolderCatGrid;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.advertisement.HomeAdSearch;
import com.adeasy.advertise.ui.advertisement.LocationPicker;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Search extends Fragment implements CategoryCallback, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String SEARCH_KEY = "search_key";
    private static final String LOCATION_SELECTED = "location_selected";

    private static final int SEARCH_BAR_RESULT = 133;
    private static final int LOCATION_PICKER = 7896;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    CategoryManager categoryManager;
    FirestorePagingAdapter<Category, ViewHolderCatGrid> firestorePagingAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    HomeViewModel homeViewModel;

    TextView locationPicker, locationName;

    CustomDialogs customDialogs;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        locationPicker = view.findViewById(R.id.locationPicker);
        locationName = view.findViewById(R.id.locationName);

        locationPicker.setOnClickListener(this);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(getActivity(), HomeAdSearch.class), SEARCH_BAR_RESULT);
            }
        });
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        try {
            if (getArguments().getString(LOCATION_SELECTED) != null)
                locationName.setText(getArguments().getString(LOCATION_SELECTED));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search Category");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryManager = new CategoryManager(this);
        mSwipeRefreshLayout = view.findViewById(R.id.gridSwipeCat);
        recyclerView = view.findViewById(R.id.GridCategoryRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        loadData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestorePagingAdapter.refresh();
            }
        });

        customDialogs = new CustomDialogs(getActivity());
    }

    public void loadData() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(4)
                .setPageSize(4)
                .build();

        FirestorePagingOptions<Category> options = new FirestorePagingOptions.Builder<Category>()
                .setLifecycleOwner(this)
                .setQuery(categoryManager.viewCategoryAll(), config, Category.class)
                .build();

        firestorePagingAdapter =
                new FirestorePagingAdapter<Category, ViewHolderCatGrid>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderCatGrid holder, final int position, @NonNull final Category category) {

                        holder.getTitleView().setText(category.getName());
                        Picasso.get().load(category.getImageUrl()).into(holder.getImageView());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Intent intent = new Intent(getContext(), NewAdvertisement.class);
                                //intent.putExtra("key", getRef(position).getKey());
                                //startActivity(intent);
                                homeViewModel.setCategoryMutableLiveData(category);
                                //Toast.makeText(view.getContext(), getItem(position).getId(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolderCatGrid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid, parent, false);
                        return new ViewHolderCatGrid(view);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                        super.onLoadingStateChanged(state);
                        switch (state) {
                            case LOADING_INITIAL:
                            case LOADING_MORE:
                                // Do your loading animation
                                mSwipeRefreshLayout.setRefreshing(true);
                                break;

                            case LOADED:
                                // Stop Animation
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case ERROR:
                                retry();
                                break;
                        }

                    }

                    @Override
                    protected void onError(@NonNull Exception e) {
                        super.onError(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        // Handle Error
                        refresh();

                    }

                };

        firestorePagingAdapter.startListening();
        recyclerView.setAdapter(firestorePagingAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        firestorePagingAdapter.startListening();

        if (!new InternetValidation().validateInternet(getActivity()))
            customDialogs.showNoInternetDialog();
    }

    @Override
    public void onClick(View view) {
        if (view == locationPicker)
            getActivity().startActivityForResult(new Intent(getActivity(), LocationPicker.class), LOCATION_PICKER);
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        firestorePagingAdapter.stopListening();
    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryManager.destroy();
    }

}