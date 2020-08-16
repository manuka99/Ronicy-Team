package com.adeasy.advertise.activity.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adeasy.advertise.R;

import androidx.appcompat.app.AppCompatActivity;

public class BugsActionsActivity extends AppCompatActivity {

    Button viewBugsBtn, addBugsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_bugs_actions);

        viewBugsBtn = findViewById(R.id.viewBugsBtn);
        addBugsBtn = findViewById(R.id.addBugsBtn);

        viewBugsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BugsActionsActivity.this, BugsActivity.class);
                startActivity(intent);
            }
        });

        addBugsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BugsActionsActivity.this, AddNewBugActivity.class);
                startActivity(intent);
            }
        });
    }
}