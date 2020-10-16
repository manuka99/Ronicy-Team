package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.util.CustomDialogs;
import com.adeasy.advertise.util.HideSoftKeyboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportAd extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "ReportAd";
    private static final String COLLECTION = "AdReports";
    public static final String AD_ID = "id";
    public static final String AD_TITLE = "title";

    Toolbar toolbar;
    TextView title;
    TextInputLayout emailView, messageView;
    Button send;
    FirebaseUser firebaseUser;
    String adId, titleName;
    String email, message;
    CustomDialogs customDialogs;
    FrameLayout frame;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_report_ad);

        frame = findViewById(R.id.frame);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        emailView = findViewById(R.id.email);
        messageView = findViewById(R.id.message);
        send = findViewById(R.id.send);
        context = this;
        customDialogs = new CustomDialogs(this);

        send.setOnClickListener(this);
        emailView.getEditText().addTextChangedListener(this);
        messageView.getEditText().addTextChangedListener(this);

        if (getIntent().hasExtra(AD_ID))
            adId = getIntent().getStringExtra(AD_ID);

        if (getIntent().hasExtra(AD_TITLE))
            titleName = getIntent().getStringExtra(AD_TITLE);

        if (adId == null || title == null) {
            Toast.makeText(getApplicationContext(), R.string.internal_error_fav, Toast.LENGTH_LONG).show();
            finish();
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24_toolbar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Report ad");

        title.setText(titleName);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null)
            emailView.getEditText().setText(firebaseUser.getEmail());
    }

    @Override
    public void onClick(View view) {
        if (view == send) {
            saveReport();
        }
    }

    private void saveReport() {
        HideSoftKeyboard.hideKeyboard(this);

        email = emailView.getEditText().getText().toString();
        message = messageView.getEditText().getText().toString();

        if (email.length() < 6)
            emailView.setError("Enter a valid email address");

        else if (message.length() < 6)
            messageView.setError("Enter a valid message");

        else {
            Map<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("message", message);
            FirebaseFirestore.getInstance().collection(COLLECTION).document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        customDialogs.showSuccessSnackbar(frame, "Your report was saved successfully. Thank you for using our services!");
                        messageView.getEditText().setText(null);
                    } else
                        customDialogs.showErrorSnackbar(frame, task.getException().getMessage());

                    HideSoftKeyboard.hideKeyboard(ReportAd.this);
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        emailView.setError(null);
        messageView.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}