package com.ddscanner.booking;


import android.app.Application;

import com.apptentive.android.sdk.Apptentive;
import com.crashlytics.android.Crashlytics;
import com.ddscanner.booking.analytics.AnalyticsSystemsManager;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;

public class DDScannerBookingApplication extends Application {

    private DDScannerRestClient ddScannerRestClient;

    public static Bus bus = new Bus();

    private static DDScannerBookingApplication instance;

    public static DDScannerBookingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ddScannerRestClient = new DDScannerRestClient();
        Apptentive.register(this, "ANDROID-DIVE-SCANNER-SCUBA-DIVIN", "d1d4a19f2a3378cfb0778a179e9d2037");
        Fabric.with(this, new Crashlytics());
        AnalyticsSystemsManager.initAnalyticsSystems(this);
    }

    public DDScannerRestClient getDdScannerRestClient() {
        return ddScannerRestClient;
    }
}
