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

public class ViewSuggestionAdmin extends AppCompatActivity {

    TextView TVsuggestionID,TVfiledDate,TVdescription,TVstatus;
    Button btnAcknowledge, btnRevoke;

    String suggestionId, description, filedDate, status;
    DatabaseReference dbref, dbref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_view_suggestion_admin);

        TVsuggestionID = findViewById(R.id.TVsuggestionID);
        TVfiledDate = findViewById(R.id.TVfiledDate);
        TVdescription = findViewById(R.id.TVdescription);
        TVstatus = findViewById(R.id.TVstatus);

        btnAcknowledge = findViewById(R.id.btnAcknowledge);
        btnRevoke = findViewById(R.id.btnRevoke);

        Intent intent = getIntent();
        suggestionId = intent.getStringExtra("SUGGESTION_ID_EXTRA");
        description = intent.getStringExtra("SUGGESTION_DESCRIPTION_EXTRA");

        TVsuggestionID.setText(suggestionId);
        TVdescription.setText(description);

        dbref= FirebaseDatabase.getInstance().getReference().child("Suggestion").child(suggestionId);

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filedDate = snapshot.child("filedDate").getValue().toString();
                status = snapshot.child("status").getValue().toString();

                TVfiledDate.setText(filedDate);
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

        dbref2 = FirebaseDatabase.getInstance().getReference().child("Suggestion").child(suggestionId);

        btnAcknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref2.child("status").setValue("Acknowledged");
                Toast.makeText(getApplicationContext(),"Suggestion Acknowledged", Toast.LENGTH_SHORT).show();
            }
        });

        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref2.child("status").setValue("Received");
                Toast.makeText(getApplicationContext(),"Revoked acknowledgement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}