package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Bug;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

public class EditBugActivity extends AppCompatActivity {

    EditText ETbugDescription;
    TextView TVbugId, TVfiledDate;
    Button btnEdit;
    DatabaseReference dbref;
    Bug bug;
    String bugId, description, filedDate, status,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_edit_bug);

        Log.i("mylog","in Edit bug activity oncreate");

        ETbugDescription = findViewById(R.id.ETBugDescription);
        TVbugId = findViewById(R.id.TVbugId);
        TVfiledDate = findViewById(R.id.TVfiledDate);
        btnEdit = findViewById(R.id.editBugBtn);

        final Intent intent = getIntent();
        bugId = intent.getStringExtra("BUGID_EXTRA");

        TVbugId.setText(bugId);

        dbref = FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){
                    ETbugDescription.setText(dataSnapshot.child("description").getValue().toString());
                    TVfiledDate.setText(dataSnapshot.child("filedDate").getValue().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"No records to show", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("mylog","in Edit bug activity in onclick of edit btn");
                DatabaseReference updref = FirebaseDatabase.getInstance().getReference().child("Bug");
                updref.child(bugId).child("description").setValue(ETbugDescription.getText().toString());

                Toast.makeText(getApplicationContext(),"Successfully updated", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(EditBugActivity.this, BugsActivity2.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                /*updref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(bugId)){
                            Log.i("mylog","in Edit bug activity in on data change");

                            bug = new Bug();
                            filedDate = dataSnapshot.child(bugId).child("filedDate").getValue().toString();
                            status = dataSnapshot.child(bugId).child("status").getValue().toString();
                            user = dataSnapshot.child(bugId).child("user").getValue().toString();

                            bug.setDescription(ETbugDescription.getText().toString());
                            bug.setBugId(bugId);
                            bug.setFiledDate(filedDate);
                            bug.setStatus(status);
                            bug.setUser(user);

                            dbref = FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);
                            dbref.setValue(bug);

                            Toast.makeText(getApplicationContext(),"Successfully updated", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(EditBugActivity.this,BugsActivity2.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No record to update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Database error", Toast.LENGTH_SHORT).show();

                    }
                });*/

            }
        });


    }
}