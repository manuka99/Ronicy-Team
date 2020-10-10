package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

public class GetInTouchActivity extends AppCompatActivity {

    Button bugsActivityBtn, suggestionsBtn, contactUsBtn, radminBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_get_in_touch);

        bugsActivityBtn = findViewById(R.id.bugReportBtn);
        suggestionsBtn = findViewById(R.id.suggestionsBtn);
        contactUsBtn  = findViewById(R.id.contactUsBtn);
        radminBtn = findViewById(R.id.btnRadmin);

        bugsActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();

                if(user == null)
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"Please login to file a bug",Toast.LENGTH_SHORT);
                    toast.show();
                }

                else
                {
                    Intent intent = new Intent(GetInTouchActivity.this, BugsActionsActivity.class);
                    startActivity(intent);
                }



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

        radminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();


                if(user == null)
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"Please login to access this section",Toast.LENGTH_SHORT);
                    toast.show();
                }

                else
                {
                    userEmail = firebaseAuth.getCurrentUser().getEmail();
                    Log.i("mylog","useremail");
                    Log.i("mylog",userEmail);

                    if(userEmail.equals("adminravi@gmail.com"))
                    {
                        Intent intent = new Intent(GetInTouchActivity.this, GetInTouchAdminPanel.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast toast=Toast.makeText(getApplicationContext(),"You do not have permission to access this section",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }
        });

    }
}