package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
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
import com.adeasy.advertise.model.Order_Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PaymentSelection extends AppCompatActivity {

    Button continueOrder, creditCardBtn, CODBtn;
    String advertisementID, categoryId;
    AdvertisementFirebase advertisementFirebase = new AdvertisementFirebaseImpl();
    CategoryFirebase categoryFirebase = new CategoryFirebaseImpl();
    Advertisement advertisement;
    Category category;
    TextView itemName, itemCat, itemPrice, itemPrice2, paymentTotal;
    ImageView adImage;
    Order order;
    boolean COD = false;
    Order_Customer customer;

    Context context;
    View myLayout;
    OrderFirebase orderFirebase = new OrderFirebaseImpl();

    private static final String TAG = "PaymentSelection";
    private static final int PAYHERE_REQUEST = 11010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);

        Gson gson = new Gson();
        customer= gson.fromJson(getIntent().getStringExtra("customer"), Order_Customer.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set up payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemName = findViewById(R.id.orderItemName);
        itemCat = findViewById(R.id.orderItemCat);
        itemPrice = findViewById(R.id.orderItemPrice);
        itemPrice2 = findViewById(R.id.orderPaymentPrice);
        paymentTotal = findViewById(R.id.orderPaymentTotal);
        adImage = findViewById(R.id.orderItemImage);

        creditCardBtn = findViewById(R.id.orderPaymentCredit);
        CODBtn = findViewById(R.id.orderPaymentCOD);

        advertisementFirebase.getAddbyID(advertisementID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        advertisement = new com.adeasy.advertise.model.Advertisement();
                        advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);
                        itemPrice.setText("Rs. " + advertisement.getPrice());
                        itemPrice2.setText("Rs. " + advertisement.getPrice());
                        paymentTotal.setText("Rs. " + advertisement.getPrice());

                        Picasso.get().load(advertisement.getImageUrl()).into(adImage);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        categoryFirebase.getCatbyID(categoryId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        category = new com.adeasy.advertise.model.Category();
                        category = document.toObject(com.adeasy.advertise.model.Category.class);

                        itemCat.setText(category.getName());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        creditCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditCardBtn.setBackgroundResource(R.drawable.button_border_green);
                CODBtn.setBackgroundResource(R.drawable.order_border);
                COD = false;
            }
        });

        CODBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CODBtn.setBackgroundResource(R.drawable.button_border_green);
                creditCardBtn.setBackgroundResource(R.drawable.order_border);
                COD = true;
            }
        });

        continueOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });


    }

    public void processPayment() {

        try {

            if (customer.getName().isEmpty() || customer.getPhone() == 0 || customer.getPhone() < 100000000 || customer.getEmail().isEmpty() || customer.getAddress().isEmpty())
                throw new Exception();

            else {

                String alert = null;

                if (COD)
                    alert = "as a cash on delivery order?";

                else
                    alert = "through our online payment gateway?";

                AlertDialog alertDialog = new AlertDialog.Builder(context)

                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setTitle("Confirm your order")

                        .setMessage("Do you want to place this order " + alert)

                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                placeOrder();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })

                        .show();


            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: Could not validate your information, please refill and try again.", Toast.LENGTH_LONG).show();
        }

    }

    public void placeOrder() {

        order = new Order();

        order.setId(orderFirebase.getDocumentNextID());

        order.getItem().setItemName(advertisement.getTitle());
        order.getItem().setImageUrl(advertisement.getImageUrl());

        order.setCustomer(customer);

        if (COD) {
            Order_Payment payment = new Order_Payment();
            payment.setType("COD");
            payment.setStatus("Processing");
            payment.setAmount(Double.valueOf(advertisement.getPrice()));
            order.setPayment(payment);
            orderFirebase.insertOrder(order, context);
        } else {

            InitRequest req = new InitRequest();
            req.setMerchantId(getApplication().getString(R.string.PAYHERE_MERCHANTID));
            req.setMerchantSecret(getApplication().getString(R.string.PAYHERE_SECRET));
            req.setCurrency("LKR");
            req.setAmount(Double.valueOf(advertisement.getPrice()));
            req.setOrderId(order.getId());
            req.setItemsDescription(order.getItem().getItemName());
            req.getCustomer().setFirstName(order.getCustomer().getName());
            req.getCustomer().setLastName("");
            req.getCustomer().setEmail(order.getCustomer().getEmail());
            req.getCustomer().setPhone(String.valueOf(order.getCustomer().getPhone()));
            req.getCustomer().getAddress().setAddress(order.getCustomer().getAddress());
            req.getCustomer().getAddress().setCity("");
            req.getCustomer().getAddress().setCountry("Sri Lanka");

            Intent intent = new Intent(myLayout.getContext(), PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            startActivityForResult(intent, PAYHERE_REQUEST);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO process response

        if (requestCode == PAYHERE_REQUEST && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            String msg;

            if (response.isSuccess()) {
                int status = response.getData().getStatus();

                if (status == 0)
                    msg = "Your payment of Rs. "+ response.getData().getPrice() + " is pending. Thank you for using our services";

                else if (status == -3)
                    msg = "Your payment of Rs. "+ response.getData().getPrice() + " was chargedback.";

                else if (status == 2 ) {
                    msg = "Your payment of Rs" + response.getData().getPrice() + " was made successfully. Thank you for supporting us!";
                    completePayhereOrder(response.getData().getPrice());
                }
                else if (status == -1)
                    msg = "Your payment of Rs. " + response.getData().getPrice() + " was cancelled.";

                else
                    msg = "Your payment of Rs. " + response.getData().getPrice() + " got failed.";

            } else
                msg = "Your payment was not made, please try again.";

        }}

    public void completePayhereOrder(Long amount){
        Order_Payment payment = new Order_Payment();
        payment.setType("Payhere");
        payment.setStatus("Approved");
        payment.setAmount(Double.valueOf(amount));
        order.setPayment(payment);
        orderFirebase.insertOrder(order, context);
    }
}