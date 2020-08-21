package com.adeasy.advertise.fragment.Order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Order_Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText nameView, phoneView, emailView, addressView;
    private String name, email, address;
    private int phone;
    private Order_Customer customer;
    private FirebaseUser firebaseUser;

    public Step1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1.
     */
    // TODO: Rename and change types and number of parameters
    public static Step1 newInstance(String param1, String param2) {
        Step1 fragment = new Step1();
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
        return inflater.inflate(R.layout.manuka_fragment_step1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameView = view.findViewById(R.id.orderName);
        emailView = view.findViewById(R.id.orderEmail);
        phoneView = view.findViewById(R.id.orderPhone);
        addressView = view.findViewById(R.id.orderAddress);
        customer = new Order_Customer();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean validateCustomer() {

        boolean result = false;

        name = nameView.getText().toString();
        email = emailView.getText().toString();
        address = addressView.getText().toString();

        try {
            phone = Integer.parseInt(phoneView.getText().toString());
        } catch (Exception e) {
            phone = 0;
        }

        if (name.isEmpty())
            nameView.setError("Please fill out your name");

        else if (phone == 0 || phone < 100000000)
            phoneView.setError("Please fill out your phone number");

        else if (email.isEmpty())
            emailView.setError("Please provide us your email");

        else if (address.isEmpty())
            addressView.setError("Please enter you delivery address");

        else{
            result = true;
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setName(name);
            if(firebaseUser != null)
            customer.setUid(firebaseUser.getUid());
        }

        return result;
    }

    public Order_Customer getCustomer(){
        return customer;
    }

}