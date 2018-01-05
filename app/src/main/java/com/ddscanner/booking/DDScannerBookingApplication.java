package com.ddscanner.booking;


import android.app.Application;

import com.ddscanner.booking.rest.DDScannerRestClient;

public class DDScannerBookingApplication extends Application {

    private DDScannerRestClient ddScannerRestClient;

    private static DDScannerBookingApplication instance;

    public static DDScannerBookingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ddScannerRestClient = new DDScannerRestClient();
    }

    public DDScannerRestClient getDdScannerRestClient() {
        return ddScannerRestClient;
    }
}
