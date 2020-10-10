package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

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
                Intent intent = new Intent(BugsActionsActivity.this, BugsActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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