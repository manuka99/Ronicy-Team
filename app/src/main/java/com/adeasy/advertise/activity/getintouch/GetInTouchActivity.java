package com.adeasy.advertise.activity.getintouch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adeasy.advertise.R;

public class GetInTouchActivity extends AppCompatActivity {

    Button bugsActivityBtn, suggestionsBtn, contactUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_get_in_touch);

        bugsActivityBtn = findViewById(R.id.bugReportBtn);
        suggestionsBtn = findViewById(R.id.suggestionsBtn);
        contactUsBtn  = findViewById(R.id.contactUsBtn);

        bugsActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetInTouchActivity.this, BugsActionsActivity.class);
                startActivity(intent);
            }
        });

        suggestionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetInTouchActivity.this, AddNewSuggestionActivity.class);
                startActivity(intent);
            }
        });

        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetInTouchActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });


    }
}