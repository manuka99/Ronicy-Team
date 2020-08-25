package com.adeasy.advertise.activity.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.fragment.Order.OrderPhoneVerify;
import com.adeasy.advertise.fragment.Order.Step1;
import com.adeasy.advertise.fragment.Order.Step2;
import com.adeasy.advertise.fragment.Order.StepSuccess;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;
import com.adeasy.advertise.model.Order_Payment;
import com.adeasy.advertise.service.MailService;
import com.adeasy.advertise.service.MailServiceImpl;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.shuhart.stepview.StepView;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class BuyNow extends AppCompatActivity implements View.OnClickListener, OrderCallback {

    Button continueOrder;
    String advertisementID, categoryId;
    Order order;
    Context context;
    Toolbar toolbar;
    StepView stepView;
    Step1 step1;
    OrderPhoneVerify orderPhoneVerify;
    Step2 step2;
    StepSuccess orderSuccess;
    MailService mailService;
    Boolean isCODSelected = false;

    private FirebaseAuth mAuth;
    private OrderManager orderManager;
    private BuynowViewModel buynowViewModel;

    private static final String TAG = "BuyNow";
    private static final int PAYHERE_REQUEST = 11010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_buy_now);

        order = new Order();
        step1 = new Step1();
        step2 = new Step2();
        orderPhoneVerify = new OrderPhoneVerify();
        orderSuccess = new StepSuccess();

        context = this;
        mAuth = FirebaseAuth.getInstance();
        mailService = new MailServiceImpl(context);
        orderManager = new OrderManager(this);
        advertisementID = getIntent().getStringExtra("aID");
        categoryId = getIntent().getStringExtra("cID");
        continueOrder = findViewById(R.id.continueOrder);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complete your purchase");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepView = findViewById(R.id.step_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buynowViewModel = ViewModelProviders.of(this).get(BuynowViewModel.class);

        buynowViewModel.getCustomer().observe(this, new Observer<Order_Customer>() {
            @Override
            public void onChanged(Order_Customer order_customer) {
                order.setCustomer(order_customer);
                startVerifyNumberFragment();
            }
        });

        buynowViewModel.getMobileNumberVerifyStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    startPaymentSelectFragment();
            }
        });

        buynowViewModel.getItem().observe(this, new Observer<Order_Item>() {
            @Override
            public void onChanged(Order_Item item) {
                order.setItem(item);
            }
        });

        buynowViewModel.getPaymentSelectCOD().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isCODSelected = aBoolean;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (getCurrentFragment() instanceof OrderPhoneVerify || getCurrentFragment() instanceof Step2) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(getCurrentFragment());
            ft.commit();

            stepView.go(0, true);
            super.onBackPressed();
        } else
            finish();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.orderStepContainer, new Step1());
        ft.commit();

        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
                .selectedCircleRadius(34)
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.colorBlack))
                .steps(new ArrayList<String>() {{
                    add("Delivery");
                    add("Confirmation");
                    add("Payment");
                }})
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(20)
                .textSize(44)
                .stepNumberTextSize(30)
                .typeface(ResourcesCompat.getFont(context, R.font.pt_serif))
                // other state methods are equal to the corresponding xml attributes
                .commit();

    }

    @Override
    public void onClick(View view) {
        if (view == continueOrder) {
            validateAndContinue();
        }
    }

    public void validateAndContinue() {

        if (getCurrentFragment() instanceof Step1) {
            buynowViewModel.setValidateCustomerDetails(true);

        } else if (getCurrentFragment() instanceof OrderPhoneVerify) {
            buynowViewModel.setStartVerifyMobileNumber(true);

        } else if (getCurrentFragment() instanceof Step2) {
            processPayment(isCODSelected);
        }

    }

    private void startVerifyNumberFragment() {
        stepView.go(1, true);

        Bundle bundle = new Bundle();
        bundle.putString("phone", String.valueOf(order.getCustomer().getPhone()));
        orderPhoneVerify.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(Step1.class.getName());
        ft.replace(R.id.orderStepContainer, orderPhoneVerify);
        ft.commit();
    }

    private void startPaymentSelectFragment() {
        stepView.go(2, true);

        Bundle bundle = new Bundle();
        bundle.putString("aID", advertisementID);
        bundle.putString("cID", categoryId);

        step2.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.orderStepContainer, step2);
        ft.commit();
    }

    private Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.orderStepContainer);
        return currentFragment;
    }

    public void processPayment(final boolean CODSelected) {

        if (validateUserBefourPayment()) {

            String alert = null;

            if (CODSelected)
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
                            placeOrder(CODSelected);
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })

                    .show();


        }

    }

    public boolean validateUserBefourPayment() {
        try {
            if (order.getItem().getItemName().isEmpty() || order.getItem().getPrice() == 0 || order.getItem().getImageUrl().isEmpty() || order.getItem().getCategoryName().isEmpty())
                return false;

            if (order.getCustomer().getName().isEmpty() || order.getCustomer().getEmail().isEmpty() || order.getCustomer().getPhone() == 0 || order.getCustomer().getAddress().isEmpty())
                return false;

            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void placeOrder(boolean CODSelected) {

        order.setId(UniqueIdBasedOnName.Generator("ORD"));

        if (CODSelected) {
            Order_Payment payment = new Order_Payment();
            payment.setType("COD");
            payment.setStatus("Processing");
            payment.setAmount(order.getItem().getPrice());
            order.setPayment(payment);
            orderManager.insertOrder(order);
        } else {
            InitRequest req = new InitRequest();
            req.setMerchantId(Configurations.PAYHERE_MERCHANTID);
            req.setMerchantSecret(Configurations.PAYHERE_SECRET);
            req.setCurrency("LKR");
            req.setAmount(order.getItem().getPrice());
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

                if (status == 2) {
                    completePayhereOrder(response.getData().getPrice());
                }

            } else
                msg = "Your payment of Rs. was not made, please try again.";

            Log.d(TAG, response.getData().toString());

        }
    }

    public void completePayhereOrder(long amount) {
        Order_Payment payment = new Order_Payment();
        payment.setType("Payhere");
        payment.setStatus("Approved");
        //payment.setAmount(Double.valueOf(amount)); //this add extra zeros
        payment.setAmount(order.getItem().getPrice());
        order.setPayment(payment);
        orderManager.insertOrder(order);
    }

    @Override
    public void onSuccessInsertOrder() {
        onSuccessOrder();
        mailService.SendOrderPlacedEmail(order);
    }

    @Override
    public void onFailureInsertOrder() {

    }

    public void onSuccessOrder() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        orderSuccess.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.orderStepContainer, orderSuccess);
        ft.commit();

        getSupportActionBar().setTitle("Order placed");

        LinearLayout stepperWrap = findViewById(R.id.steperWraper);
        LinearLayout continueWrap = findViewById(R.id.continueWrapOrder);
        stepperWrap.setVisibility(View.GONE);
        continueWrap.setVisibility(View.GONE);
        continueOrder.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderManager.destroy();
        mailService.destroy();
    }

}