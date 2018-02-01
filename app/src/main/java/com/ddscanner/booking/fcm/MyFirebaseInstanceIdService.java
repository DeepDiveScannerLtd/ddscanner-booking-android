package com.ddscanner.booking.fcm;


import com.apptentive.android.sdk.Apptentive;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Apptentive.setPushNotificationIntegration(Apptentive.PUSH_PROVIDER_APPTENTIVE, token);
    }
}
