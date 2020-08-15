package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.activity.athentication.login;
import com.adeasy.advertise.activity.athentication.register;
import com.adeasy.advertise.firebase.AdvertisementFirebase;
import com.adeasy.advertise.firebase.AdvertisementFirebaseImpl;
import com.adeasy.advertise.firebase.CategoryFirebase;
import com.adeasy.advertise.firebase.CategoryFirebaseImpl;
import com.adeasy.advertise.firebase.OrderFirebase;
import com.adeasy.advertise.firebase.OrderFirebaseImpl;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;
import com.adeasy.advertise.model.Order_Payment;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PlaceOrder extends AppCompatActivity {

    Button continueOrder, creditCardBtn, CODBtn;
    Order_Customer customer;
    EditText nameView, phoneView, emailView, addressView;
    String name, email, address;
    int phone;
    String advertisementID, categoryId;
    AdvertisementFirebase advertisementFirebase = new AdvertisementFirebaseImpl();
    CategoryFirebase categoryFirebase = new CategoryFirebaseImpl();
    Advertisement advertisement;
    Category category;
    TextView itemName, itemCat, itemPrice, itemPrice2, paymentTotal;
    ImageView adImage;
    Order order;
    boolean COD = false;

    Context context;
    View myLayout;

    private FirebaseAuth mAuth;
    OrderFirebase orderFirebase = new OrderFirebaseImpl();

    private static final String TAG = "PlaceOrder";
    private static final int PAYHERE_REQUEST = 11010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        context= this;
        mAuth = FirebaseAuth.getInstance();

        advertisementID = getIntent().getStringExtra("aID");
        categoryId = getIntent().getStringExtra("cID");
        continueOrder = findViewById(R.id.continueOrder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complete your purchase");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.orderStepContent);
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.order_layout_one, mainLayout, false);
        mainLayout.addView(myLayout);

        nameView = findViewById(R.id.orderName);
        emailView = findViewById(R.id.orderEmail);
        phoneView = findViewById(R.id.orderPhone);
        addressView = findViewById(R.id.orderAddress);

        continueOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCustomerDetails();
            }
        });

    }

    public void validateCustomerDetails() {

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

        else {
            customer = new Order_Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null)
                order.getCustomer().setUid(user.getUid());

            Intent intent = new Intent(context, PaymentSelection.class);
            Gson gson = new Gson();
            intent.putExtra("customer", gson.toJson(customer));
            intent.putExtra("aID", advertisementID);
            intent.putExtra("CID", categoryId);
            startActivity(intent);

        }

    }


}