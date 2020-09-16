package com.adeasy.advertise.ui.administration.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.helper.ViewHolderOrderItem;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.ui.administration.advertisement.MoreActionsOnAd;
import com.adeasy.advertise.ui.editAd.EditAd;
import com.adeasy.advertise.util.CustomDialogs;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastOrders extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OrderManager orderManager;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirestorePagingAdapter<Order, ViewHolderOrderItem> firestorePagingAdapter;
    FirestorePagingOptions<Order> options;
    Button online, cod;
    Query query;
    String pending_order_section;
    Boolean completed;
    CustomDialogs customDialogs;

    private static final String FRAGMENT_SECTION_KEY = "pending_order_section";
    private static final String COMPLETED = "completed";
    private static final String CANCELLED = "cancelled";

    public PastOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PastOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static PastOrders newInstance(String param1, String param2) {
        PastOrders fragment = new PastOrders();
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
        View view = inflater.inflate(R.layout.manuka_admin_fragment_past_orders, container, false);

        recyclerView = view.findViewById(R.id.myaddsRecycle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMyadds);

        online = view.findViewById(R.id.online);
        cod = view.findViewById(R.id.cod);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderManager = new OrderManager();
        customDialogs = new CustomDialogs(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        online.setOnClickListener(this);
        cod.setOnClickListener(this);

        try {
            pending_order_section = getArguments().getString(FRAGMENT_SECTION_KEY);
        } catch (Exception e) {
            pending_order_section = COMPLETED;
        }

        if (pending_order_section.equals(COMPLETED))
            completed = true;

        else
            completed = false;

        onOnlineOrdersUi();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == online) {
            onOnlineOrdersUi();
        } else if (view == cod) {
            onCodOrdersUi();
        }
    }

    private void onOnlineOrdersUi() {
        online.setBackgroundResource(R.drawable.grey_btn_half_left_round);
        cod.setBackgroundResource(R.drawable.red_btn_half_round);
        query = orderManager.codPastOrders(completed, false);
        loadData();
    }

    private void onCodOrdersUi() {
        cod.setBackgroundResource(R.drawable.grey_btn_half_right_round);
        online.setBackgroundResource(R.drawable.blue_btn_half_round);
        query = orderManager.codPastOrders(completed, true);
        loadData();
    }

    public void loadData() {
        if (query == null)
            Toast.makeText(getActivity(), "Error: please try again later", Toast.LENGTH_LONG).show();
        else {
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(3)
                    .build();

            options = new FirestorePagingOptions.Builder<Order>()
                    .setLifecycleOwner(this)
                    .setQuery(query, config, Order.class)
                    .build();

            firestorePagingAdapter =
                    new FirestorePagingAdapter<Order, ViewHolderOrderItem>(
                            options
                    ) {
                        @Override
                        protected void onBindViewHolder(@NonNull ViewHolderOrderItem holder, final int position, @NonNull Order order) {
                            holder.getName().setText(order.getItem().getItemName());
                            holder.getPrice().setText("Rs " + order.getItem().getPrice());
                            holder.getCategory().setText(order.getItem().getCategoryName());

                            holder.getPlaced_date().setText(order.getPreetyTime());
                            holder.getPayment_mode().setText(order.getPayment().getType());
                            holder.getPayment_status().setText(order.getPayment().getStatus());

                            holder.getTotal().setText("Rs " + order.getItem().getPrice());
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

                        @NonNull
                        @Override
                        public ViewHolderOrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_order_item_layout, parent, false);
                            return new ViewHolderOrderItem(view);
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
                            //refresh();

                            if (e instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) e).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
                                customDialogs.showPermissionDeniedStorage();

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
    }


}