package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.shuhart.stepview.StepView;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
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
    double paymentAmount;
    Context context;
    Toolbar toolbar;
    StepView stepView;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complete your purchase");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.orderStepCont);
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.order_layout_one, mainLayout, false);
        mainLayout.addView(myLayout);

        nameView = findViewById(R.id.orderName);
        emailView = findViewById(R.id.orderEmail);
        phoneView = findViewById(R.id.orderPhone);
        addressView = findViewById(R.id.orderAddress);

        stepView = findViewById(R.id.step_view);

        continueOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCustomerDetails();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
                .selectedCircleRadius(34)
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.colorBlack))
                .steps(new ArrayList<String>() {{
                    add("Delivery");
                    add("Payment");
                    add("Confirmation");
                }})
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(20)
                .textSize(44)
                .stepNumberTextSize(30)
                .typeface(ResourcesCompat.getFont(context, R.font.pt_serif))
                // other state methods are equal to the corresponding xml attributes
                .commit();

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

            stepView.go(1, true);

            FrameLayout mainLayout = (FrameLayout) findViewById(R.id.orderStepCont);
            LayoutInflater inflater = getLayoutInflater();
            View myLayout = inflater.inflate(R.layout.order_layout_two, mainLayout, false);
            mainLayout.addView(myLayout);

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

                            itemName.setText(advertisement.getTitle());
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

    }


    public void processPayment() {

        try {

            if (name.isEmpty() || phone == 0 || phone < 100000000 || email.isEmpty() || address.isEmpty())
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
        order.getItem().setCategoryName(category.getName());
        order.getItem().setPrice(advertisement.getPrice());

        order.getCustomer().setName(name);
        order.getCustomer().setPhone(phone);
        order.getCustomer().setEmail(email);
        order.getCustomer().setAddress(address);

        paymentAmount = advertisement.getPrice();

        if (COD) {
            Order_Payment payment = new Order_Payment();
            payment.setType("COD");
            payment.setStatus("Processing");
            payment.setAmount(paymentAmount);
            order.setPayment(payment);
            saveToFirebase(order);
        } else {
            InitRequest req = new InitRequest();
            req.setMerchantId(getApplication().getString(R.string.PAYHERE_MERCHANTID));
            req.setMerchantSecret(getApplication().getString(R.string.PAYHERE_SECRET));
            req.setCurrency("LKR");
            req.setAmount(paymentAmount);
            req.setOrderId(order.getId());
            req.setItemsDescription(order.getItem().getItemName());
            req.getCustomer().setFirstName(order.getCustomer().getName());
            req.getCustomer().setLastName("");
            req.getCustomer().setEmail(order.getCustomer().getEmail());
            req.getCustomer().setPhone(String.valueOf(order.getCustomer().getPhone()));
            req.getCustomer().getAddress().setAddress(order.getCustomer().getAddress());
            req.getCustomer().getAddress().setCity("");
            req.getCustomer().getAddress().setCountry("Sri Lanka");

            Intent intent = new Intent(this, PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID like private final static int PAYHERE_REQUEST = 11010;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO process response
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
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
                msg = "Your payment of Rs. was not made, please try again.";

            Log.d(TAG, response.getData().toString());

        }}

    public void completePayhereOrder(long amount){
        Order_Payment payment = new Order_Payment();
        payment.setType("Payhere");
        payment.setStatus("Approved");
        //payment.setAmount(Double.valueOf(amount));
        payment.setAmount(paymentAmount);
        order.setPayment(payment);
        saveToFirebase(order);
    }

    public void saveToFirebase(final Order order){

        String id = orderFirebase.getDocumentNextID();
        DocumentReference ref = orderFirebase.getInsertQueryOrder(id);

        order.setId(id);

        ref.set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "Success: Your order was placed", Toast.LENGTH_LONG).show();
                        try {
                            orderFirebase.uploadImage(order);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        inflateSuccessOrderView(order);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    public void inflateSuccessOrderView(Order order){

        TextView orderedItemName, orderedItemCat, orderedItemPrice, orderedItemPrice2, orderedaymentTotal, orderID;
        ImageView orderedItemImage;

        getSupportActionBar().setTitle("Order placed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout linearLayout = findViewById(R.id.steperWraper);
        linearLayout.setVisibility(View.GONE);

        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.orderStepCont);
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.order_layout_three, mainLayout, false);
        mainLayout.addView(myLayout);

        orderID = findViewById(R.id.orderID);
        orderedItemName = findViewById(R.id.orderedItemName);
        orderedItemCat = findViewById(R.id.orderedItemCat);
        orderedItemPrice = findViewById(R.id.orderedItemPrice);
        orderedItemPrice2 = findViewById(R.id.orderedPaymentPrice);
        orderedaymentTotal = findViewById(R.id.orderedPaymentTotal);
        orderedItemImage = findViewById(R.id.orderedItemImage);

        orderID.setText(order.getId());
        orderedItemName.setText(order.getItem().getItemName());
        orderedItemCat.setText(order.getItem().getCategoryName());
        orderedItemPrice.setText("Rs. " + order.getItem().getPrice());
        orderedItemPrice2.setText("Rs. " + order.getItem().getPrice());
        orderedaymentTotal.setText("Rs. " + order.getPayment().getAmount());

        Picasso.get().load(order.getItem().getImageUrl()).into(orderedItemImage);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}