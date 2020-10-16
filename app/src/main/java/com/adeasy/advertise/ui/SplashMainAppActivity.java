package com.adeasy.advertise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.administration.home.DashboardHome;
import com.adeasy.advertise.ui.advertisement.Advertisement;
import com.adeasy.advertise.ui.advertisement.Myadds;
import com.adeasy.advertise.ui.home.MainActivity;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.adeasy.advertise.ui.profile.Profile;
import com.adeasy.advertise.util.CommonConstants;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class SplashMainAppActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    private static int SPLASH_TIMEOUT = 1000;

    private static final String TAG = "SplashMainAppActivity";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.manuka_activity_splash_main_app);
        linearLayout = findViewById(R.id.linearlayoutSplashMain);
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout_main);
        linearLayout.startAnimation(aniSlide);

        intent = new Intent(SplashMainAppActivity.this, MainActivity.class);


        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_INTENT)) {
            intent = handleCloudMessaging();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_INTENT)) {
            RemoteMessage remoteMessage = getIntent().getParcelableExtra(CommonConstants.CLOUD_MESSAGING_DATA);
            //intent.putExtra(CommonConstants.CLOUD_MESSAGING_DATA, remoteMessage);
            Log.i(TAG, "new intent");
            Log.i(TAG, "Messaging data: " + remoteMessage.getData());
        }
    }

    private Intent handleCloudMessaging() {
        Intent intent = null;
        try {
            if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_INTENT)) {
                if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_ADMIN))
                    intent = new Intent(getApplicationContext(), DashboardHome.class);
                else if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_NEWPOST))
                    intent = new Intent(getApplicationContext(), NewAd.class);
                else if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_PROFILE))
                    intent = new Intent(getApplicationContext(), Profile.class);
                else if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_MYADs))
                    intent = new Intent(getApplicationContext(), Myadds.class);
                else if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_AD)) {
                    intent = new Intent(getApplicationContext(), Advertisement.class);
                    if (getIntent().hasExtra(Advertisement.ADVERTISEMENTID))
                        intent.putExtra(Advertisement.ADVERTISEMENTID, getIntent().getStringExtra(Advertisement.ADVERTISEMENTID));
                    if (getIntent().hasExtra(Advertisement.CATID))
                        intent.putExtra(Advertisement.CATID, getIntent().getStringExtra(Advertisement.CATID));
                } else if (getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_INTENT).equals(CommonConstants.CLOUD_MESSAGE_DIALOG)) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);

                    //a dialog must have a body
                    if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_BODY)) {
                        String header = null, body = null, image = null;

                        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_HEADER))
                            header = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_HEADER);

                        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_BODY))
                            body = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_BODY);

                        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_IMAGE))
                            image = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_IMAGE);

                        intent.putExtra(CommonConstants.CLOUD_MESSAGE_HEADER, header);
                        intent.putExtra(CommonConstants.CLOUD_MESSAGE_BODY, body);
                        intent.putExtra(CommonConstants.CLOUD_MESSAGE_IMAGE, image);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }

}