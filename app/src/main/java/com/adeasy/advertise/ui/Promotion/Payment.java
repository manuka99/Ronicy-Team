package com.adeasy.advertise.ui.Promotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.PromotionsViewModel;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.callback.PromotionCallback;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.manager.PromotionManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.adeasy.advertise.util.Promotions;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class Payment extends AppCompatActivity implements PromotionCallback, View.OnClickListener, TextWatcher {

    Toolbar toolbar;
    Button continueBTN;
    String advertisementID;
    LinearLayout continueLayout, mainLayout;
    ProgressBar progressBar;
    PromotionManager promotionManager;
    FrameLayout promoFrame;
    PromoBody promoBody;

    //total
    double totalSum;

    //payment summary
    LinearLayout paymentSummary;

    TextInputLayout nameView, phoneView, emailView;

    String promoID;
    String name;
    String phone;
    String email;

    Map<Integer, Integer> promos;
    Map<String, Integer> promosString;

    //view model
    PromotionsViewModel promotionsViewModel;

    Bundle bundle;

    private static final String TAG = "Payment";

    private static final String PROMOS_ADDED = "promos_added";
    private static final String ADVERTISEMENT_ID = "adID";
    private static final String PROMOTION_ID = "promoID";
    private static final int PAYHERE_REQUEST = 8562;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_promotion_activity_payment);

        //main layouts
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);
        promoFrame = findViewById(R.id.promoFrame);
        continueLayout = findViewById(R.id.continueLayout);
        continueBTN = findViewById(R.id.continueBTN);

        //payment summary
        paymentSummary = findViewById(R.id.paymentSummary);

        nameView = paymentSummary.findViewById(R.id.name);
        phoneView = paymentSummary.findViewById(R.id.phone);
        emailView = paymentSummary.findViewById(R.id.email);

        //hide all and load
        continueLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        paymentSummary.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        continueBTN.setBackgroundResource(R.drawable.button_round_grey);

        //text watchers
        nameView.getEditText().addTextChangedListener(this);
        phoneView.getEditText().addTextChangedListener(this);
        emailView.getEditText().addTextChangedListener(this);

        promotionManager = new PromotionManager(this);

        promoBody = new PromoBody();

        //checkIntent values and bundle
        bundle = new Bundle();
        if (getIntent().hasExtra(PROMOS_ADDED)) {
            promos = (Map<Integer, Integer>) getIntent().getSerializableExtra(PROMOS_ADDED);
            convertToStringMap();
            bundle.putSerializable(PROMOS_ADDED, (Serializable) promos);
        }

        if (getIntent().hasExtra(ADVERTISEMENT_ID)) {
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);
            bundle.putString(ADVERTISEMENT_ID, advertisementID);
        }

        //toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //set listeners
        continueBTN.setOnClickListener(this);

        promotionsViewModel = ViewModelProviders.of(this).get(PromotionsViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        promoBody.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(promoFrame.getId(), promoBody).commit();
        promotionsViewModel.getTotal().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                totalSum = aDouble;
                //show layout
                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                continueLayout.setVisibility(View.VISIBLE);

                if (totalSum > 0) {
                    continueBTN.setBackgroundResource(R.drawable.button_round_fb);
                    paymentSummary.setVisibility(View.VISIBLE);
                } else {
                    continueBTN.setBackgroundResource(R.drawable.button_round_grey);
                    paymentSummary.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == continueBTN)
            checkout();
    }

    private boolean validatePaymentDetails() {
        if (nameView.getEditText().getText().length() < 6) {
            nameView.setError("Your name is not clear enough!");
            nameView.requestFocus();
        } else if (phoneView.getEditText().getText().length() < 9 || phoneView.getEditText().getText().length() > 14) {
            phoneView.setError("Phone number seems to be invalid!");
            phoneView.requestFocus();
        } else if (emailView.getEditText().getText().length() < 10) {
            emailView.setError("Your email address seems to be invalid!");
            emailView.requestFocus();
        } else {
            name = nameView.getEditText().getText().toString();
            phone = phoneView.getEditText().getText().toString();
            email = emailView.getEditText().getText().toString();
            return true;
        }
        return false;
    }

    private void checkout() {
        if (totalSum > 0 && validatePaymentDetails()) {
            //place the promotion order

            promoID = UniqueIdBasedOnName.Generator("PROMO");

            String itemPromos = "";

            for (Integer key : promos.keySet()) {
                itemPromos += "Promo type = " + key + " Days = " + promos.get(key) + " ";
            }

            InitRequest req = new InitRequest();
            req.setMerchantId(Configurations.PAYHERE_MERCHANTID);
            req.setMerchantSecret(Configurations.PAYHERE_SECRET);
            req.setCurrency("LKR");
            req.setAmount(totalSum);
            req.setOrderId(promoID);
            req.setItemsDescription(itemPromos);
            req.getCustomer().setFirstName(name);
            req.getCustomer().setLastName("");
            req.getCustomer().setEmail(email);
            req.getCustomer().setPhone(phone);
            req.getCustomer().getAddress().setAddress("");
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
                    completePayhereCheckOut();
                }

            } else
                msg = "Your payment of Rs. was not made, please try again.";

            Log.d(TAG, response.getData().toString());

        }
    }

    private void completePayhereCheckOut() {
        promotionManager.savePromotions(new Promotion(promoID, advertisementID, promosString));
        Intent intent  = new Intent(this, PaymentMade.class);
        intent.putExtra(PROMOTION_ID, promoID);
        intent.putExtra(ADVERTISEMENT_ID, advertisementID);
        intent.putExtra(PROMOS_ADDED, (Serializable) promos);
        startActivity(intent);
        finish();
    }

    private void convertToStringMap() {
        promosString = new HashMap<>();
        for (Integer key : promos.keySet()) {
            promosString.put(String.valueOf(key), promos.get(key));
        }
    }

    @Override
    public void onCompleteSavePromotion(Task<Void> task) {

    }

    @Override
    public void onGetPromotionByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onPromotionsListIds(List<String> ids, Query promotionQuery) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        nameView.setError(null);
        phoneView.setError(null);
        emailView.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}