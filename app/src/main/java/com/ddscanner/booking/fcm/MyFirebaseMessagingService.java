package com.ddscanner.booking.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apptentive.android.sdk.Apptentive;
import com.apptentive.android.sdk.ApptentiveLog;
import com.ddscanner.booking.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        logPushBundle(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        if (Apptentive.isApptentivePushNotification(data)) {
            Apptentive.buildPendingIntentFromPushNotification(new Apptentive.PendingIntentCallback() {
                @Override
                public void onPendingIntent(PendingIntent pendingIntent) {
                    if (pendingIntent != null) {
                        String title = Apptentive.getTitleFromApptentivePush(data);
                        String body = Apptentive.getBodyFromApptentivePush(data);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notificationBuilder.build());
                    } else {
                        // Push notification was not for the active conversation. Do nothing.
                    }
                }
            }, data);
        } else {
            // This push did not come from Apptentive. It should be handled by your app.
        }
    }

    private static void logPushBundle(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        for (String key : data.keySet()) {
            String value = data.get(key);
        }
    }
}

