package com.adeasy.advertise.ui.Promotion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.PromotionsViewModel;

import java.io.Serializable;
import java.util.Map;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PaymentMade extends AppCompatActivity {

    private static final String PROMOS_ADDED = "promos_added";
    private static final String ADVERTISEMENT_ID = "adID";
    private static final String PROMOTION_ID = "promoID";

    Map<Integer, Integer> promos;
    Map<String, Integer> promosString;

    //view model
    PromotionsViewModel promotionsViewModel;

    Bundle bundle;

    PromoBody promoBody;

    String advertisementID, promotionID;

    FrameLayout promoFrame;
    TextView promotionIDView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_payment_made);

        //checkIntent values and bundle
        bundle = new Bundle();
        if (getIntent().hasExtra(PROMOS_ADDED)) {
            promos = (Map<Integer, Integer>) getIntent().getSerializableExtra(PROMOS_ADDED);
            bundle.putSerializable(PROMOS_ADDED, (Serializable) promos);
        }

        if (getIntent().hasExtra(ADVERTISEMENT_ID)) {
            advertisementID = getIntent().getStringExtra(ADVERTISEMENT_ID);
            bundle.putString(ADVERTISEMENT_ID, advertisementID);
        }

        if (getIntent().hasExtra(PROMOTION_ID)) {
            promotionID = getIntent().getStringExtra(PROMOTION_ID);
        }

        promoBody = new PromoBody();

        toolbar = findViewById(R.id.toolbar);
        promoFrame = findViewById(R.id.promoFrame);
        promotionIDView = findViewById(R.id.promotionID);
        promotionIDView.setText(promotionID);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Completed");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        promoBody.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(promoFrame.getId(), promoBody).commit();
    }
}