package com.adeasy.advertise.ui.administration.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.OrderDashboardViewModel;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Dashboard extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OrderDashboardViewModel orderDashboardViewModel;

    LinearLayout recentOrders, online_orders, cod_orders, cancelled_orders, completed_orders, documentation, sales, product_sales;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        View view = inflater.inflate(R.layout.manuka_admin_orders_fragment_dashboard, container, false);
        recentOrders = view.findViewById(R.id.recent);
        online_orders = view.findViewById(R.id.onlinePending);
        cod_orders = view.findViewById(R.id.codPending);
        cancelled_orders = view.findViewById(R.id.cancelled);
        completed_orders = view.findViewById(R.id.completed);
        documentation = view.findViewById(R.id.documentation);
        sales = view.findViewById(R.id.monthAnalysis);
        product_sales = view.findViewById(R.id.productAnalysis);

        recentOrders.setOnClickListener(this);
        online_orders.setOnClickListener(this);
        cod_orders.setOnClickListener(this);
        cancelled_orders.setOnClickListener(this);
        completed_orders.setOnClickListener(this);
        documentation.setOnClickListener(this);
        sales.setOnClickListener(this);
        product_sales.setOnClickListener(this);

        orderDashboardViewModel = ViewModelProviders.of(getActivity()).get(OrderDashboardViewModel.class);

        return view;
    }

    @Override
    public void onClick(View view) {
        orderDashboardViewModel.setSelectedCategory(view.getId());
    }

}