package com.adeasy.advertise.ui.Order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Order;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepSuccess#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepSuccess extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView orderedItemName, orderedItemCat, orderedItemPrice, orderedItemPrice2, orderedaymentTotal, orderID;
    ImageView orderedItemImage;
    Order order;

    public StepSuccess() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepSuccess.
     */
    // TODO: Rename and change types and number of parameters
    public static StepSuccess newInstance(String param1, String param2) {
        StepSuccess fragment = new StepSuccess();
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
        View view = inflater.inflate(R.layout.manuka_fragment_step_success, container, false);

        orderID = view.findViewById(R.id.orderID);
        orderedItemName = view.findViewById(R.id.orderedItemName);
        orderedItemCat = view.findViewById(R.id.orderedItemCat);
        orderedItemPrice = view.findViewById(R.id.orderedItemPrice);
        orderedItemPrice2 = view.findViewById(R.id.orderedPaymentPrice);
        orderedaymentTotal = view.findViewById(R.id.orderedPaymentTotal);
        orderedItemImage = view.findViewById(R.id.orderedItemImage);

        order = (Order) getArguments().getSerializable("order");

        displayOrderedItem();

        return view;
    }

    public void displayOrderedItem(){
        orderID.setText(order.getId());
        orderedItemName.setText(order.getItem().getItemName());
        orderedItemCat.setText(order.getItem().getCategoryName());
        orderedItemPrice.setText("Rs. " + order.getItem().getPrice());
        orderedItemPrice2.setText("Rs. " + order.getItem().getPrice());
        orderedaymentTotal.setText("Rs. " + order.getPayment().getAmount());
        Picasso.get().load(order.getItem().getImageUrl()).into(orderedItemImage);
    }

}