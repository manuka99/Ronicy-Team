package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.helper.ViewHolderOrderItem;
import com.adeasy.advertise.helper.ViewHolderOrderItemHome;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.ui.administration.order.MoreOnOrder;
import com.adeasy.advertise.ui.athentication.LoginRegister;
import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.CustomDialogs;
import com.facebook.common.Common;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Orders extends Fragment implements OrderCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Orders";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth firebaseAuth;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    FirestorePagingAdapter<Order, ViewHolderOrderItemHome> firestorePagingAdapter;
    FirestorePagingOptions<Order> options;

    OrderManager orderManager;

    CustomDialogs customDialogs;
    SwipeRefreshLayout swipeRefreshRecycle_view;

    public Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Orders");

        frameLayout = view.findViewById(R.id.frameLayout);
        recyclerView = view.findViewById(R.id.recycle_view);
        swipeRefreshRecycle_view = view.findViewById(R.id.swipeRefreshRecycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();

        customDialogs = new CustomDialogs(getActivity());
        orderManager = new OrderManager(this, getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
            loadData();
        else
            getFragmentManager().beginTransaction().replace(frameLayout.getId(), new LoginRegister()).commit();
    }

    public void loadData() {
        orderManager.getCount(orderManager.myOrders());
        if (firestorePagingAdapter != null)
            firestorePagingAdapter.startListening();
        else {
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(3)
                    .build();

            options = new FirestorePagingOptions.Builder<Order>()
                    .setLifecycleOwner(this)
                    .setQuery(orderManager.myOrders(), config, Order.class)
                    .build();

            firestorePagingAdapter =
                    new FirestorePagingAdapter<Order, ViewHolderOrderItemHome>(
                            options
                    ) {
                        @Override
                        protected void onBindViewHolder(@NonNull ViewHolderOrderItemHome holder, final int position, @NonNull Order order) {

                            if (order.getOrderStatus().equals(CommonConstants.ORDER_DELIVERED))
                                holder.deliveredLayout.setVisibility(View.VISIBLE);
                            else
                                holder.deliveredLayout.setVisibility(View.GONE);

                            holder.orderID.setText(order.getId());
                            holder.order_time.setText(order.getPlacedDate().toString());
                            holder.orderType.setText(order.getPayment().getType());
                            holder.orderStatus.setText(order.getOrderStatus());
                            holder.orderPaymentMode.setText(order.getPayment().getStatus());

                            holder.orderItemName.setText(order.getItem().getItemName());
                            holder.orderItemCat.setText(order.getItem().getCategoryName());
                            holder.orderItemPrice.setText(String.valueOf(order.getItem().getPrice()));

                            holder.address.setText(order.getCustomer().getAddress());
                            holder.email.setText(order.getCustomer().getEmail());
                            holder.mobile.setText(String.valueOf(order.getCustomer().getPhone()));

                            Picasso.get().load(order.getItem().getImageUrl()).fit().into(holder.imageView);
                        }

                        @NonNull
                        @Override
                        public ViewHolderOrderItemHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_my_order_item_layout, parent, false);
                            return new ViewHolderOrderItemHome(view);
                        }

                        @Override
                        protected void onLoadingStateChanged(@NonNull com.firebase.ui.firestore.paging.LoadingState state) {
                            super.onLoadingStateChanged(state);
                            switch (state) {
                                case LOADING_INITIAL:
                                case LOADING_MORE:
                                    // Do your loading animation
                                    swipeRefreshRecycle_view.setRefreshing(true);
                                    break;

                                case LOADED:
                                    // Stop Animation
                                    swipeRefreshRecycle_view.setRefreshing(false);
                                    break;

                                case FINISHED:
                                    swipeRefreshRecycle_view.setRefreshing(false);
                                    break;

                                case ERROR:
                                    retry();
                                    break;
                            }
                        }

                        @Override
                        protected void onError(@NonNull Exception e) {
                            super.onError(e);
                            swipeRefreshRecycle_view.setRefreshing(false);
                            e.printStackTrace();
                            //Handle Error
                            //refresh();

                            if (e instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) e).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
                                customDialogs.showPermissionDeniedStorage();

                        }

                    };

            firestorePagingAdapter.startListening();
            recyclerView.setAdapter(firestorePagingAdapter);

            swipeRefreshRecycle_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    firestorePagingAdapter.refresh();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestorePagingAdapter != null)
            firestorePagingAdapter.stopListening();
    }

    @Override
    public void onCompleteInsertOrder(Task<Void> task) {

    }

    @Override
    public void onGetOrderByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onHideOrderByID(Task<Void> task) {

    }

    @Override
    public void onDeleteOrderByID(Task<Void> task) {

    }

    @Override
    public void getAllOrdersByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void onOrderCount(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Total orders: " + task.getResult().size());
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            Exception e = task.getException();
            if (e instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) e).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
                customDialogs.showPermissionDeniedStorage();
        }
    }

}