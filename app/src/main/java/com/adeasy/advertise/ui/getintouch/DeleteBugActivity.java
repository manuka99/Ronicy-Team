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
import com.adeasy.advertise.model.Bug;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

public class DeleteBugActivity extends AppCompatActivity {

    TextView TVBugId, TVdescription;
    Button btnDelete;
    //DatabaseReference dbref;
    String bugId,description;

    Bug bug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_delete_bug);

        Log.i("mylog","in delete bug activity in on create");

        TVBugId = findViewById(R.id.TVBugIdDeleteBugActivity);
        TVdescription = findViewById(R.id.TVDescriptionDeleteBugActivity);

        btnDelete = findViewById(R.id.btnDelete);

        Intent intent =  getIntent();
        bugId = intent.getStringExtra("BUGID_EXTRA");
        description = intent.getStringExtra("BUG_DESCRIPTION_EXTRA");

        TVBugId.setText(bugId);
        TVdescription.setText(description);



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("mylog","in delete bug activity in delete button onclick");
                DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("Bug");

                //preveously here, delref.addValueEventListner was active

                delref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(bugId)){
                            /*dbref = FirebaseDatabase.getInstance().getReference().child("Bug").child(bugId);
                            dbref.removeValue();*/

                            Log.i("mylog","in delete bug activity in on data change");

                            dataSnapshot.child(bugId).getRef().removeValue(); //this was added later removing using snapshot
                            Log.i("mylog","in delete bug activity bug deleted");
                            Toast.makeText(getApplicationContext(),"Bug report deleted succesfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DeleteBugActivity.this, BugsActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            Log.i("mylog","in delete bug activity new intent created");

                            startActivity(intent);
                            //finish();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No source to delete", Toast.LENGTH_SHORT).show();
                            Log.i("mylog","in delete bug activity no source to delete");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });


    }
}