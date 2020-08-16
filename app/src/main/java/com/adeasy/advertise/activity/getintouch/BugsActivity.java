package com.adeasy.advertise.activity.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adeasy.advertise.R;

import androidx.appcompat.app.AppCompatActivity;

public class BugsActivity extends AppCompatActivity {

    Button viewBugBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_bugs);

        viewBugBtn = findViewById(R.id.viewBugBtn1);

        viewBugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BugsActivity.this, ViewBugActivity.class);
                startActivity(intent);
            }
        });

    }
}