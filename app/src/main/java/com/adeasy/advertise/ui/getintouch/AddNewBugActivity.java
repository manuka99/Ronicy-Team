package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Bug;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewBugActivity extends AppCompatActivity {

    EditText bugDescription;
    Button btnSubmit;
    DatabaseReference dbref;
    Bug bug = new Bug();
    String newID;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    String usernameStr,dateTimeStr, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_add_new_bug);

        dbref = FirebaseDatabase.getInstance().getReference().child("Bug");
        String key  = dbref.getKey();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = format.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("mylog","Could not parse date");
        }

        dateTimeStr = date.toString();


        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long children = dataSnapshot.getChildrenCount();
                long newChild = ++children;
                newID = "b" + newChild;

                while (dataSnapshot.hasChild(newID ) ){
                    newChild = ++children;
                    newID = "b" + newChild;

                }


                Log.i("mylog","children: " + children);
                Log.i("mylog","newid: " + newID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bugDescription = findViewById(R.id.EtDescribeBug);
        btnSubmit = findViewById(R.id.btnNewBugSubmit);
        dbref = FirebaseDatabase.getInstance().getReference().child("Bug");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(bugDescription.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter a description", Toast.LENGTH_SHORT).show();
                }
                else{
                    //dbref.child("bug1");
                    bug.setBugId(newID); //previously b100
                    bug.setDescription(bugDescription.getText().toString());

                    usernameStr = user.getEmail();
                    bug.setUser(usernameStr);

                    bug.setStatus("Pending");

                    bug.setFiledDate(dateTimeStr);
                    dbref.child(newID).setValue(bug);  //previously bug1
                    Toast.makeText(getApplicationContext(),"Bug info sent", Toast.LENGTH_SHORT).show();
                    clearControls();

                    Intent intent = new Intent(getApplicationContext(), BugsActionsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        });


    }

    public  void clearControls(){
        bugDescription.setText("");
    }

    public void generateNewId(){
        dbref = FirebaseDatabase.getInstance().getReference().child("Bug");
        String key  = dbref.getKey();



        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long children = dataSnapshot.getChildrenCount();
                long newChild = children++;
                String newID = "b" + newChild;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}