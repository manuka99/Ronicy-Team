package com.adeasy.advertise.ui.advertisement;

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
import lk.payhere.androidsdk.model.StatusResponse;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Donations extends AppCompatActivity {

    private final static int PAYHERE_REQUEST = 11010;
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

               // else
                    //makePayment();
            }
        });

    }

}