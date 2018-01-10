package com.ddscanner.booking.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.ddscanner.booking.BuildConfig;
import com.ddscanner.booking.R;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

public class AnalyticsSystemsManager {

    private static FirebaseAnalytics firebaseAnalytics;
//    private static GoogleAnalytics analytics;
//    private static Tracker googleAnalyticsEventsTracker;

    private AnalyticsSystemsManager() {

    }

    public static void initAnalyticsSystems(Context context) {
        // Init Google Firebase
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG);
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(true);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        // Init flurry
        FlurryAgent.init(context, context.getString(R.string.flurry_api_key));
        if (!TextUtils.isEmpty(FirebaseInstanceId.getInstance().getId())) {
            FlurryAgent.setUserId(FirebaseInstanceId.getInstance().getId());
        }

        // Google analytics
//        analytics = GoogleAnalytics.getInstance(context);
//        googleAnalyticsEventsTracker = analytics.newTracker(R.string.google_analytics_trackingId);


    }

    public static void setUserIdForAnalytics(String userAppId) {
        // Flurry
        FlurryAgent.setUserId(userAppId);
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }

//    public static Tracker getGoogleAnalyticsEventsTracker() {
//        return googleAnalyticsEventsTracker;
//    }

}
