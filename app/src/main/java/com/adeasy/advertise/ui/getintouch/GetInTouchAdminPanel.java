package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

public class GetInTouchAdminPanel extends AppCompatActivity {

    Button btnViewBugs, btnViewSuggestions;
    TextView TVbugCount, TVsuggestionsCount;

    DatabaseReference dbrefBug, dbrefSuggestion;
    long bugCount, suggestionsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_get_in_touch_admin_panel);

        btnViewBugs = findViewById(R.id.btnViewBugs);
        btnViewSuggestions = findViewById(R.id.btnViewSuggestions);
        TVbugCount = findViewById(R.id.TVbugCount);
        TVsuggestionsCount = findViewById(R.id.TVsuggestionsCount);

        dbrefBug = FirebaseDatabase.getInstance().getReference().child("Bug");
        dbrefSuggestion = FirebaseDatabase.getInstance().getReference().child("Suggestion");

        dbrefBug.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bugCount = snapshot.getChildrenCount();
                TVbugCount.setText(String.valueOf(bugCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbrefSuggestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                suggestionsCount = snapshot.getChildrenCount();
                TVsuggestionsCount.setText(String.valueOf(suggestionsCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnViewBugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAllBugsAdmin.class);
                startActivity(intent);
            }
        });

        btnViewSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), ViewAllSuggestionsAdmin.class);
                startActivity(intent);
            }
        });

    }

    public void bugStatisticSwitch(View view)
    {
        Intent intent = new Intent(getApplicationContext(),BugStatisticsActivity.class);
        startActivity(intent);
    }
}