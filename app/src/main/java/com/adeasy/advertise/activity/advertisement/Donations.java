package com.adeasy.advertise.activity.advertisement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adeasy.advertise.R;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class Donations extends AppCompatActivity {

    private final static int PAYHERE_REQUEST = 11010;
    private final static String PAYHERE_MERCHANTID = "1214373";
    private final static String PAYHERE_SECRET = "8bIpNGLREQb4UobgTUm9Dj49Z3dIeUkau4DrwsH4olA0";
    private static final String TAG = "Donations";
    EditText name, email, phone, amount;
    TextView donateSuccess;
    Button donateBtn;
    String userName, userEmail;
    Integer userPhone;
    double userAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);

        donateSuccess = findViewById(R.id.donateSuccess);
        donateBtn = findViewById(R.id.donateBtn);

        name = findViewById(R.id.donationName);
        email = findViewById(R.id.donationEmail);
        phone = findViewById(R.id.donationNumber);
        amount = findViewById(R.id.donationAmount);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donate Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = email.getText().toString();
                userEmail = email.getText().toString();

                try{
                    userPhone = Integer.parseInt(phone.getText().toString());
                }catch (Exception e){
                    userPhone = 0;
                }

                try{
                    userAmount = Double.parseDouble(amount.getText().toString());
                }catch (Exception e){
                    userAmount = 0;
                }

                if(userName.isEmpty())
                    name.setError("Please fill out this feild");

                else if(userEmail.isEmpty())
                    email.setError("Please provide a valid email");

                else if(userPhone < 100000000)
                    phone.setError("Please provide a valid phone number");

                else if(userAmount < 100)
                    amount.setError("Minimum donation cannot be less than Rs 100.00");

                else
                    makePayment();
            }
        });

    }

    private void makePayment(){
        InitRequest req = new InitRequest();
        req.setMerchantId(PAYHERE_MERCHANTID);
        req.setMerchantSecret(PAYHERE_SECRET);
        req.setCurrency("LKR");
        req.setAmount(userAmount);
        req.setOrderId("");
        req.setItemsDescription("Donations");
        req.getCustomer().setFirstName(userName);
        req.getCustomer().setLastName("");
        req.getCustomer().setEmail(userEmail);
        req.getCustomer().setPhone(userPhone.toString());
        req.getCustomer().getAddress().setAddress("");
        req.getCustomer().getAddress().setCity("");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
        startActivityForResult(intent, PAYHERE_REQUEST);
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
                    name.setText("");
                    email.setText("");
                    phone.setText("");
                    amount.setText("");
                }
                else if (status == -1)
                    msg = "Your payment of Rs. " + response.getData().getPrice() + " was cancelled.";

                else
                    msg = "Your payment of Rs. " + response.getData().getPrice() + " got failed.";

            } else
                msg = "Your payment of Rs. " + response.getData().getPrice() + " was not made, please try again.";

            Log.d(TAG, response.getData().toString());

            donateSuccess.setVisibility(View.VISIBLE);
            donateSuccess.setText(msg);
        }
    }

}