package com.adeasy.advertise.ui.administration.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderOrderItem;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PendingOrders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FRAGMENT_SECTION_KEY = "pending_order_section";
    private static final String RECENT = "recent";
    private static final String ONLINE_PAYMENTS = "online";
    private static final String COD_DELIVERY = "cod";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OrderManager orderManager;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CustomDialogs customDialogs;

    TextView header;

    CardView noDataLayout;

    //this is to save the bundle value in order to show which content
    String pending_order_section;

    public PendingOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingOrders newInstance(String param1, String param2) {
        PendingOrders fragment = new PendingOrders();
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
        noDataLayout = view.findViewById(R.id.noDataLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMyadds);
        header = view.findViewById(R.id.header);

        try {
            pending_order_section = getArguments().getString(FRAGMENT_SECTION_KEY);
        } catch (Exception e) {
            pending_order_section = RECENT;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderManager = new OrderManager();
        firebaseAuth = FirebaseAuth.getInstance();
        customDialogs = new CustomDialogs(getActivity());
        loadData(getQueryForFragment());
        setToolbarSubTitle();
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

    public void loadData(Query query) {
        FirestoreRecyclerOptions<Order> options =
                new FirestoreRecyclerOptions.Builder<Order>()
                        .setQuery(query, Order.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Order, ViewHolderOrderItem>(options) {
            @Override
            public void onBindViewHolder(ViewHolderOrderItem holder, final int position, Order order) {
                holder.getName().setText(order.getItem().getItemName());
                holder.getPrice().setText(order.getItem().getPreetyCurrency());
                holder.getCategory().setText(order.getItem().getCategoryName());

                holder.getPlaced_date().setText(order.getPreetyTime());
                holder.getPayment_mode().setText(order.getPayment().getType());
                holder.getPayment_status().setText(order.getPayment().getStatus());

                holder.getTotal().setText(order.getPayment().getPreetyCurrency());
                Picasso.get().load(order.getItem().getImageUrl()).fit().into(holder.getImageView());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MoreOnOrder.class);
                        intent.putExtra("orderID", getItem(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ViewHolderOrderItem onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.manuka_order_item_layout, group, false);

                return new ViewHolderOrderItem(view);
            }

            @Override
            public void onDataChanged() {
                swipeRefreshLayout.setRefreshing(true);
                // Called each time there is a new query snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                if (getSnapshots().size() == getItemCount())
                    swipeRefreshLayout.setRefreshing(false);

                if (getSnapshots().size() == 0)
                    noDataLayout.setVisibility(View.VISIBLE);
                else
                    noDataLayout.setVisibility(View.GONE);
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

    private Query getQueryForFragment() {
        Query query;
        if (pending_order_section.equals(ONLINE_PAYMENTS))
            query = orderManager.onlinePendingOrders();
        else if (pending_order_section.equals(COD_DELIVERY))
            query = orderManager.codPendingOrders();
        else
            query = orderManager.recentOrders();
        return query;
    }

    private void setToolbarSubTitle() {
        if (pending_order_section.equals(ONLINE_PAYMENTS)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Pending orders - Online Payments");
            header.setText(R.string.pending_online_orders);
        } else if (pending_order_section.equals(COD_DELIVERY)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Pending orders - COD Orders");
            header.setText(R.string.pending_cod_orders);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Pending orders - Recent");
            header.setText(R.string.pending_recent_orders);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}