package com.adeasy.advertise.cloudMessaging;/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;

import com.adeasy.advertise.R;
import com.adeasy.advertise.manager.SharedPreferencesManager;
import com.adeasy.advertise.ui.SplashMainAppActivity;
import com.adeasy.advertise.ui.administration.home.DashboardHome;
import com.adeasy.advertise.ui.administration.order.Dashboard;
import com.adeasy.advertise.ui.home.MainActivity;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.adeasy.advertise.ui.profile.Profile;
import com.adeasy.advertise.util.CommonConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String BROADCAST_MESSAGE_TOKEN = "cloud_messaging_broadcast";
    public static final String BROADCAST_CLOUD_MESSAGE = "cloud_message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        handleNow(remoteMessage);
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        //send broadcast

        getApplicationContext().sendBroadcast(new Intent(BROADCAST_MESSAGE_TOKEN));

        new SharedPreferencesManager(getApplicationContext()).setCloudMessagingToken(token);
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(RemoteMessage remoteMessage) {
        com.adeasy.advertise.cloudMessaging.NotificationManager notificationManager = new com.adeasy.advertise.cloudMessaging.NotificationManager(getApplicationContext());

////         Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            Intent intent = new Intent(BROADCAST_CLOUD_MESSAGE);
//            intent.putExtra(CommonConstants.CLOUD_MESSAGING_DATA, remoteMessage);
//            getApplicationContext().sendBroadcast(intent);
//        }

        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        notificationManager.sendNotification(remoteMessage.getNotification().getBody(), getCloudDataIntent(remoteMessage));
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
//    private void sendNotification(String messageBody, Intent intent) {
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.rlogo)
//                        .setContentTitle(getString(R.string.fcm_message))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
    private Intent getCloudDataIntent(RemoteMessage remoteMessage) {
        Intent intent = new Intent(getApplicationContext(), SplashMainAppActivity.class);
        for (String key : remoteMessage.getData().keySet()) {
            intent.putExtra(key, remoteMessage.getData().get(key));
        }
        return intent;
    }

}