package com.adeasy.advertise.ui.Order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.callback.ProfileManagerCallback;
import com.adeasy.advertise.callback.VerifiedNumbersCallback;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.manager.ProfileManager;
import com.adeasy.advertise.manager.VerifiedNumbersManager;
import com.adeasy.advertise.model.User;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.Order_Item;
import com.adeasy.advertise.model.Order_Payment;
import com.adeasy.advertise.model.UserVerifiedNumbers;
import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.InternetValidation;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class BuyNow extends AppCompatActivity implements View.OnClickListener, OrderCallback, ProfileManagerCallback {

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
    Boolean isCODSelected = false;
    Boolean isNumberVerified = false;
    Boolean isUpdatingDeliveryDetails = true;
    Boolean isOrderCompleted = false;

    User user;

    CustomDialogs customDialogs;

    private FirebaseAuth mAuth;
    private OrderManager orderManager;
    private ProfileManager profileManager;
    private BuynowViewModel buynowViewModel;

    private static final String TAG = "BuyNow";
    private static final String CUSTOMER = "customer";
    private static final int PAYHERE_REQUEST = 11010;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_buy_now);

        order = new Order(true);
        step1 = new Step1();
        step2 = new Step2();
        orderPhoneVerify = new OrderPhoneVerify();
        orderSuccess = new StepSuccess();

        context = this;
        mAuth = FirebaseAuth.getInstance();
        orderManager = new OrderManager(this, this);
        profileManager = new ProfileManager(this);
        customDialogs = new CustomDialogs(this);
        advertisementID = getIntent().getStringExtra("aID");
        categoryId = getIntent().getStringExtra("cID");
        continueOrder = findViewById(R.id.continueOrder);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complete your purchase");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepView = findViewById(R.id.step_view);

        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
                //.selectedCircleRadius(34)
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.colorBlack))
                .steps(new ArrayList<String>() {{
                    add("Delivery");
                    add("Confirmation");
                    add("Payment");
                }})
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                //.stepLineWidth(14)
                //.textSize(30)
                //.stepNumberTextSize(34)
                .typeface(ResourcesCompat.getFont(context, R.font.pt_serif))
                // other state methods are equal to the corresponding xml attributes
                .commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (mAuth.getCurrentUser() != null)
            profileManager.getUser();

        buynowViewModel = ViewModelProviders.of(this).get(BuynowViewModel.class);

        buynowViewModel.getCustomer().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User order_customer) {
                isNumberVerified = false;
                isUpdatingDeliveryDetails = false;
                order.setCustomer(order_customer);
                startVerifyNumberFragment();
            }
        });

        buynowViewModel.getMobileNumberVerifyStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isNumberVerified = aBoolean;
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

        buynowViewModel.getVisibilityContinue().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    continueOrder.setVisibility(View.VISIBLE);
                else
                    continueOrder.setVisibility(View.GONE);
            }
        });

        handelStep1Fragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!new InternetValidation().validateInternet(context))
            customDialogs.showNoInternetDialog();
        else
            validateFragmentOnStart();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof Step1)
            showExitDialog();
        if (getCurrentFragment() instanceof StepSuccess)
            finish();
        else {
            stepView.go(0, true);
            handelStep1Fragment();
            isUpdatingDeliveryDetails = true;
        }
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

        } else if (getCurrentFragment() instanceof Step2) {
            processPayment(isCODSelected);
        }
    }

    private void startVerifyNumberFragment() {
        orderPhoneVerify = new OrderPhoneVerify();
        stepView.go(1, true);

        Bundle bundle = new Bundle();
        bundle.putString("phone", String.valueOf(order.getCustomer().getPhone()));
        orderPhoneVerify.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.orderStepContainer, orderPhoneVerify).commit();
    }

    private void startPaymentSelectFragment() {
        step2 = new Step2();
        stepView.go(2, true);

        Bundle bundle = new Bundle();
        bundle.putString("aID", advertisementID);
        bundle.putString("cID", categoryId);

        step2.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.orderStepContainer, step2).commit();
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

            new AlertDialog.Builder(context)

                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

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

    private void handelStep1Fragment() {
        Bundle bundle = new Bundle();
        if (order != null && order.getCustomer() != null && order.getCustomer().getEmail() != null)
            bundle.putSerializable(CUSTOMER, order.getCustomer());
        else if (user != null)
            bundle.putSerializable(CUSTOMER, user);
        else if (mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            user = new User();
            user.setEmail(firebaseUser.getEmail());
            user.setName(firebaseUser.getDisplayName());
            user.setPhone(firebaseUser.getPhoneNumber());
            bundle.putSerializable(CUSTOMER, user);
        }
        Step1 step1 = new Step1();
        step1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.orderStepContainer, step1).commit();
    }

    public boolean validateUserBefourPayment() {
        try {
            if (order.getItem().getItemName().isEmpty() || order.getItem().getPrice() == 0 || order.getItem().getImageUrl().isEmpty() || order.getItem().getCategoryName().isEmpty())
                return false;

            if (order.getCustomer().getName().isEmpty() || order.getCustomer().getEmail().isEmpty() || order.getCustomer().getPhone() == null || order.getCustomer().getPhone().isEmpty() || order.getCustomer().getAddress().isEmpty())
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
            payment.setType(CommonConstants.PAYMENT_COD);
            payment.setAmount(order.getItem().getPrice());
            order.setPayment(payment);
            orderManager.insertOrder(order, true);
            isOrderCompleted = true;
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
        payment.setType(CommonConstants.PAYMENT_PAYHERE);
        payment.setStatus(CommonConstants.PAYMENT_PAID);
        //payment.setAmount(Double.valueOf(amount)); //this add extra zeros
        payment.setAmount(order.getItem().getPrice());
        order.setPayment(payment);
        orderManager.insertOrder(order, true);
        isOrderCompleted = true;
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
    }

    @Override
    public void onCompleteInsertOrder(Task<Void> task) {
        if (task != null && task.isSuccessful())
            onSuccessOrder();
        else
            Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetOrderByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onHideOrderByID(Task<Void> task) {

    }

    @Override
    public void onDeleteOrderByID(Task<Void> task) {

    }

    @Override
    public void getAllOrdersByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void onOrderCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void onSuccessUpdateProfile(Void aVoid) {

    }

    @Override
    public void onFailureUpdateProfile(Exception e) {

    }

    @Override
    public void onCompleteUpdatePassword(Task<Void> task) {

    }

    @Override
    public void onCompleteUpdateEmail(Task<Void> task) {

    }

    @Override
    public void onTaskFull(boolean status) {

    }

    @Override
    public void onCompleteGetUser(Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful() && task.getResult() != null)
            user = task.getResult().toObject(User.class);
        else if (task != null && task.getException() instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) task.getException()).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
            customDialogs.showPermissionDeniedStorage();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this).setTitle("Are you sure you want to exit?").setMessage("Note: Your details/changes made will not be saved.").setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void validateFragmentOnStart() {
        if (isOrderCompleted)
            onSuccessOrder();
        else if (order.getCustomer().getEmail() == null || isUpdatingDeliveryDetails)
            handelStep1Fragment();
        else if (!isNumberVerified)
            startVerifyNumberFragment();
        else
            startPaymentSelectFragment();
    }

}