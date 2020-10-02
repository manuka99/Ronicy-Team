package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ViewBugActivity extends AppCompatActivity {

    Button editBtn, deleteBtn;
    TextView TVbugId, TVbugDescription, TVstatus, TVfiledDate;

    //Bug bug= new Bug();
    String bugId, description, filedDate, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_view_bug);
        Log.i("mylog","in oncreate begining of view bug activity");

        editBtn = findViewById(R.id.editBugBtn);
        deleteBtn = findViewById(R.id.deleteBugBtn);

        TVfiledDate = findViewById(R.id.TVfiledDate);
        TVstatus = findViewById(R.id.TVStatus);
        TVbugId = findViewById(R.id.TVbugIdActivityVIewBug);
        TVbugDescription = findViewById(R.id.TVBugDescriprionActivityViewBug);

        Log.i("mylog","in view bug activity getting extra");
        Intent intent = getIntent();
        bugId = intent.getStringExtra("BUGID_EXTRA");
        Log.i("mylog","in view bug activity got string extra");
        Log.i("mylog","in view bug activity got bug id: " + bugId);
        TVbugId.setText(bugId);

        description = intent.getStringExtra("BUG_DESCRIPTION_EXTRA");
        TVbugDescription.setText(description);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);  //previously "bug1"
        Log.i("mylog","dbref created");

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("mylog","in ondatachange");
                if(dataSnapshot.hasChildren()){
                    Log.i("mylog","in view bug activity data snapshot has children");
                    filedDate = dataSnapshot.child("filedDate").getValue().toString();
                    status = dataSnapshot.child("status").getValue().toString();

                    TVfiledDate.setText(filedDate);
                    TVstatus.setText(status);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No data to display",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBugActivity.this, EditBugActivity.class);
                intent.putExtra("BUGID_EXTRA",bugId);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 =  new Intent(ViewBugActivity.this, DeleteBugActivity.class);
                intent1.putExtra("BUGID_EXTRA",bugId);
                intent1.putExtra("BUG_DESCRIPTION_EXTRA",description);
                startActivity(intent1);
            }
        });
    }
}