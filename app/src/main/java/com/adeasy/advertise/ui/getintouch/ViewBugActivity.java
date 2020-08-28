package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adeasy.advertise.R;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBugActivity extends AppCompatActivity {

    Button editBtn, deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_view_bug);

        editBtn = findViewById(R.id.editBugBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBugActivity.this, EditBugActivity.class);
                startActivity(intent);
            }
        });
    }
}