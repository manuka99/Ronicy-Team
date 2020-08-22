package com.adeasy.advertise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.activity.advertisement.MainActivity;

public class SplashMainAppActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    private static int SPLASH_TIMEOUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.manuka_activity_splash_main_app);
        linearLayout = findViewById(R.id.linearlayoutSplashMain);
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomout_main);
        linearLayout.startAnimation(aniSlide);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashMainAppActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}