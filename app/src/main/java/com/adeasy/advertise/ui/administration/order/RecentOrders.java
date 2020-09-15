package com.adeasy.advertise.ui.administration.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.helper.ViewModelOrderItem;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.ui.administration.advertisement.MoreActionsOnAd;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentOrders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OrderManager orderManager;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CustomDialogs customDialogs;

    public RecentOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentOrders newInstance(String param1, String param2) {
        RecentOrders fragment = new RecentOrders();
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
        View view = inflater.inflate(R.layout.manuka_admin_orders_fragment_recent_orders, container, false);
        recyclerView = view.findViewById(R.id.myaddsRecycle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMyadds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderManager = new OrderManager();
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
        FirestoreRecyclerOptions<Order> options =
                new FirestoreRecyclerOptions.Builder<Order>()
                        .setQuery(FirebaseFirestore.getInstance().collection("Order"), Order.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Order, ViewModelOrderItem>(options) {
            @Override
            public void onBindViewHolder(ViewModelOrderItem holder, final int position, Order order) {
                holder.getName().setText(order.getItem().getItemName());
                holder.getPrice().setText("Rs " + order.getItem().getPrice());
                holder.getCategory().setText(order.getItem().getCategoryName());

                holder.getPlaced_date().setText(order.getPreetyTime());
                holder.getPayment_mode().setText(order.getPayment().getType());
                holder.getPayment_status().setText(order.getPayment().getStatus());

                holder.getTotal().setText("Rs " + order.getItem().getPrice());
                Picasso.get().load(order.getItem().getImageUrl()).fit().into(holder.getImageView());
            }

            @Override
            public ViewModelOrderItem onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.manuka_order_item_layout, group, false);

                return new ViewModelOrderItem(view);
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