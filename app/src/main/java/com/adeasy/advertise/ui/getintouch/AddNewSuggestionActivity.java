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
import com.adeasy.advertise.model.Suggestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewSuggestionActivity extends AppCompatActivity {

    EditText inputSuggestion;
    Button btnSubmitSuggestion;
    Suggestion suggestion = new Suggestion();
    DatabaseReference dbref;
    String newID, dateTimeStr,status;
    String suggestionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_add_new_suggestion);

        Log.i("mylog","in add new suggestions activity oncreate");

        inputSuggestion = findViewById(R.id.EditTextSuggestions);
        btnSubmitSuggestion = findViewById(R.id.btnSubmitSuggesstions);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = format.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("mylog","Could not parse date");
        }

        dateTimeStr = date.toString();
        status= "Received";

        dbref = FirebaseDatabase.getInstance().getReference().child("Suggestion");

        Log.i("mylog","in add new suggestions activity dbref created");
        String key  = dbref.getKey();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long children = dataSnapshot.getChildrenCount();
                long newChild = ++children;
                newID = "sg" + newChild;

                while (dataSnapshot.hasChild(newID ) ){
                    newChild = ++children;
                    newID = "sg" + newChild;

                }

                Log.i("mylog","in add new suggestions activity newID:"+newID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbref = FirebaseDatabase.getInstance().getReference().child("Suggestion");

        btnSubmitSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(inputSuggestion.getText().toString())){
                    Log.i("mylog","in add new suggestions activity onclick empty suggestion");
                    Toast.makeText(getApplicationContext(),"Please enter a description for suggestion", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.i("mylog","in add new suggestions activity valid suggestion");
                    suggestionDescription = inputSuggestion.getText().toString();
                    Log.i("mylog","in add new suggestions activity description string set");

                    suggestion.setSuggestionID(newID);
                    Log.i("mylog","in add new suggestions activity id set in object");

                    suggestion.setSuggestionDescription(suggestionDescription);
                    suggestion.setFiledDate(dateTimeStr);
                    suggestion.setStatus(status);

                    Log.i("mylog","in add new suggestions activity - onclick- suggestion object set with values");

                    dbref.child(newID).setValue(suggestion);
                    Log.i("mylog","in add new suggestions activity - onclick- added new suggestion");


                    Toast.makeText(getApplicationContext(),"Suggestion recorded. Thank you!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), GetInTouchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.i("mylog","in add new suggestions activity - onclick- intent flag set");
                    startActivity(intent);
                }
            }
        });
    }
}