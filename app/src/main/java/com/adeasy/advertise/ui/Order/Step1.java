package com.adeasy.advertise.ui.Order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
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
    private static final String TAG = "Step1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText nameView, phoneView, emailView, addressView;
    private String name, email, address;
    private Integer phone;
    private Order_Customer customer;
    private FirebaseUser firebaseUser;
    private BuynowViewModel buynowViewModel;

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
        View view = inflater.inflate(R.layout.manuka_fragment_step1, container, false);
        nameView = view.findViewById(R.id.orderName);
        emailView = view.findViewById(R.id.orderEmail);
        phoneView = view.findViewById(R.id.orderPhone);
        addressView = view.findViewById(R.id.orderAddress);
        customer = new Order_Customer();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buynowViewModel = ViewModelProviders.of(getActivity()).get(BuynowViewModel.class);
        buynowViewModel.getValidateCustomerDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED){
                    if (aBoolean)
                        startValidation();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        buynowViewModel.setVisibilityContinue(true);
    }

    private void startValidation() {
        if (validateCustomer())
            buynowViewModel.setCustomer(customer);
    }

    private boolean validateCustomer() {

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

        else if (phone < 100000000 || phone > 1000000000)
            phoneView.setError("Please fill out a valid phone number");

        else if (email.isEmpty())
            emailView.setError("Please provide us your email");

        else if (address.isEmpty())
            addressView.setError("Please enter you delivery address");

        else {
            result = true;
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setName(name);
            if (firebaseUser != null)
                customer.setUid(firebaseUser.getUid());
        }

        return result;
    }

}