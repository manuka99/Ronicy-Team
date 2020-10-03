package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewBugAdmin extends AppCompatActivity {

    TextView TVbugID,TVfiledDate,TVdescription,TVstatus,TVuser;
    Button btnAcknowledge,btnFixed,btnRevoke;

    String bugId, description, filedDate, status,user;
    DatabaseReference dbref, dbref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_view_bug_admin);

        TVbugID = findViewById(R.id.TVbugID);
        TVfiledDate = findViewById(R.id.TVfiledDate);
        TVdescription = findViewById(R.id.TVdescription);
        TVstatus = findViewById(R.id.TVstatus);
        TVuser = findViewById(R.id.TVuser);

        btnAcknowledge = findViewById(R.id.btnAcknowledge);
        btnFixed = findViewById(R.id.btnFixed);
        btnRevoke = findViewById(R.id.btnRevoke);

        Intent intent = getIntent();
        bugId = intent.getStringExtra("BUGID_EXTRA");
        description = intent.getStringExtra("BUG_DESCRIPTION_EXTRA");

        TVbugID.setText(bugId);
        TVdescription.setText(description);

        dbref= FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filedDate = snapshot.child("filedDate").getValue().toString();
                user = snapshot.child("user").getValue().toString();
                status = snapshot.child("status").getValue().toString();

                TVfiledDate.setText(filedDate);
                TVuser.setText(user);
                TVstatus.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = snapshot.child("status").getValue().toString();
                TVstatus.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbref2 = FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);

        btnAcknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref2.child("status").setValue("Acknowledged");
                Toast.makeText(getApplicationContext(),"Bug Acknowledged", Toast.LENGTH_SHORT).show();
            }
        });

        btnFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref2.child("status").setValue("Fixed");
                Toast.makeText(getApplicationContext(),"Bug marked fixed", Toast.LENGTH_SHORT).show();
            }
        });

        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref2.child("status").setValue("Pending");
                Toast.makeText(getApplicationContext(),"Revoked bug status to pending", Toast.LENGTH_SHORT).show();
            }
        });

    }
}