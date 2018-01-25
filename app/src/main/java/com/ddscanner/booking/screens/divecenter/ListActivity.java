package com.ddscanner.booking.screens.divecenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.models.DailyTourDetails;
import com.ddscanner.booking.models.FunDiveDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.certificate.courses.CertificateCoursesAdapter;
import com.ddscanner.booking.screens.course.CourseDetailsActivity;
import com.ddscanner.booking.screens.dailytour.TourDetailsActivity;
import com.ddscanner.booking.screens.fundives.FunDiveDetailsActivity;
import com.ddscanner.booking.screens.results.courses.CoursesListAdapter;
import com.ddscanner.booking.screens.results.dailytours.DailyToursListAdapter;
import com.ddscanner.booking.screens.results.fundives.FunDivesListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends BaseAppCompatActivity implements DialogClosedListener {

    private DDScannerRestClient.ResultListener<ArrayList<CourseDetails>> certificateCoursesResultListener = new DDScannerRestClient.ResultListener<ArrayList<CourseDetails>>() {
        @Override
        public void onSuccess(ArrayList<CourseDetails> result) {
            dataLoaded();
            certificateCoursesAdapter.setCourseDetails(result);
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private DDScannerRestClient.ResultListener<ArrayList<CourseDetails>> coursesResultListener = new DDScannerRestClient.ResultListener<ArrayList<CourseDetails>>() {
        @Override
        public void onSuccess(ArrayList<CourseDetails> result) {
            coursesListAdapter.setCources(result);
            dataLoaded();
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>> funDivesResultListener = new DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>>() {
        @Override
        public void onSuccess(ArrayList<FunDiveDetails> result) {
            funDivesListAdapter.setFunDives(result);
            dataLoaded();
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>> dailyToursResultListener = new DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>>() {
        @Override
        public void onSuccess(ArrayList<DailyTourDetails> result) {
            dailyToursListAdapter.setData(result);
            dataLoaded();
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private static final String ARG_SOURCE = "source";
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";

    public static void show(Context context, Source source, long id) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(ARG_SOURCE, source);
        intent.putExtra(ARG_ID, id);
        context.startActivity(intent);
    }

    public static void show(Context context, Source source, long id, String name) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(ARG_SOURCE, source);
        intent.putExtra(ARG_ID, id);
        intent.putExtra(ARG_NAME, name);
        context.startActivity(intent);
    }

    public enum Source {
        DAILY_TOUR, FUN_DIVE, COURSE, CERTIFICATE_COURSS
    }

    @BindView(R.id.progressBar)
    ProgressView progressView;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private Source source;
    private DailyToursListAdapter dailyToursListAdapter;
    private FunDivesListAdapter funDivesListAdapter;
    private CoursesListAdapter coursesListAdapter;
    private CertificateCoursesAdapter certificateCoursesAdapter;
    private long dcId;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divecenter_list);
        ButterKnife.bind(this);
        source = (Source) getIntent().getSerializableExtra(ARG_SOURCE);
        dcId = getIntent().getLongExtra(ARG_ID, -1);
        name = getIntent().getStringExtra(ARG_NAME);
        setupUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        LatLngBounds bounds = DDScannerBookingApplication.getInstance().getDdScannerRestClient().getLatLngBounds();
        if (bounds != null) {
            outState.putBoolean("contains_bounds", true);
            outState.putDouble("latLngBounds.northeast.latitude", bounds.northeast.latitude);
            outState.putDouble("latLngBounds.northeast.longitude", bounds.northeast.longitude);
            outState.putDouble("latLngBounds.southwest.latitude", bounds.southwest.latitude);
            outState.putDouble("latLngBounds.southwest.longitude", bounds.southwest.longitude);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("contains_bounds")) {
            LatLng southwest = new LatLng(savedInstanceState.getDouble("latLngBounds.southwest.latitude"), savedInstanceState.getDouble("latLngBounds.southwest.longitude"));
            LatLng northeast = new LatLng(savedInstanceState.getDouble("latLngBounds.northeast.latitude"), savedInstanceState.getDouble("latLngBounds.northeast.longitude"));
            LatLngBounds bounds = new LatLngBounds(southwest, northeast);
            DDScannerBookingApplication.getInstance().getDdScannerRestClient().setLatLngBounds(bounds);
        }
    }

    private void dataLoaded() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setupUi() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        switch (source) {
            case COURSE:
                setupToolbar(R.string.courses, R.id.toolbar, true);
                coursesListAdapter = new CoursesListAdapter(item -> CourseDetailsActivity.show(this, item.getId()));
                recyclerView.setAdapter(coursesListAdapter);
                DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenterCourses(coursesResultListener, dcId);
                break;
            case FUN_DIVE:
                setupToolbar("Fun diving", R.id.toolbar, true);
                funDivesListAdapter = new FunDivesListAdapter(item -> FunDiveDetailsActivity.show(this, item.getId()));
                recyclerView.setAdapter(funDivesListAdapter);
                DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenterFunDives(funDivesResultListener, dcId);
                break;
            case DAILY_TOUR:
                setupToolbar(R.string.daily_tours, R.id.toolbar, true);
                dailyToursListAdapter = new DailyToursListAdapter(item -> TourDetailsActivity.show(this, item.getId()));
                recyclerView.setAdapter(dailyToursListAdapter);
                DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenterDailyTours(dailyToursResultListener, dcId);
                break;
            case CERTIFICATE_COURSS:
                Log.i("Certificate courses", "Tracked certificate courses screen, id = " + dcId + " name = " + name);
                EventsTracker.trackEventNameCertificateDiveCentersScreenView(dcId, name);
                setupToolbar(name, R.id.toolbar, true);
                certificateCoursesAdapter = new CertificateCoursesAdapter();
                recyclerView.setAdapter(certificateCoursesAdapter);
                DDScannerBookingApplication.getInstance().getDdScannerRestClient().getCertificateCourses(certificateCoursesResultListener, dcId);
                break;
        }
    }

    @Override
    public void onDialogClosed(int requestCode) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
