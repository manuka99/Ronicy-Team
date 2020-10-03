package com.adeasy.advertise.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AboutUsMainActivity extends AppCompatActivity {


    EditText Description;
    EditText AdminName;
    EditText AdminEmail;
    EditText Contact;
    Button submitBtn;

    DatabaseReference dbref;

    AboutUs abtUs;

    private void clearControls(){

        Description.setText("");
        AdminName.setText("");
        AdminEmail.setText("");
        Contact.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ashan_activity_chat_main);

        Description = findViewById(R.id.aboutDescription);
        AdminName = findViewById(R.id.AdminName1);
        AdminEmail = findViewById(R.id.AdminEmail);
        Contact = findViewById(R.id.AdminContact);

        submitBtn = findViewById(R.id.abtbutton1);


        AboutUs abtUs = new AboutUs();

        dbref = FirebaseDatabase.getInstance().getReference().child("AboutUs");

        try{

            if(TextUtils.isEmpty(AdminName.getText().toString()))
                Toast.makeText(getApplicationContext(),"Please enter Admin Name",Toast.LENGTH_SHORT).show();

            else if(TextUtils.isEmpty(AdminEmail.getText().toString()))
                Toast.makeText(getApplicationContext(),"Please enter Admin Email",Toast.LENGTH_SHORT).show();

            else if(TextUtils.isEmpty(Contact.getText().toString()))
                Toast.makeText(getApplicationContext(),"Please enter Contact Number",Toast.LENGTH_SHORT).show();

            else{

                abtUs.setName(AdminName.getText().toString().trim());
                abtUs.setName(AdminEmail.getText().toString().trim());
                abtUs.setName(Contact.getText().toString().trim());

                dbref.push().setValue(abtUs);

                Toast.makeText(getApplicationContext(),"Data Saved Successfully", Toast.LENGTH_SHORT).show();
                clearControls();

            }


        }

catch (NumberFormatException e){
    Toast.makeText(getApplicationContext(),"Invalid Contact Number", Toast.LENGTH_SHORT).show();
}


    }



}