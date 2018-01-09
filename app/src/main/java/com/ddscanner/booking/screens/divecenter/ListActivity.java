package com.ddscanner.booking.screens.divecenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.models.DailyTourDetails;
import com.ddscanner.booking.models.FunDiveDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.course.CourseDetailsActivity;
import com.ddscanner.booking.screens.dailytour.TourDetailsActivity;
import com.ddscanner.booking.screens.fundives.FunDiveDetailsActivity;
import com.ddscanner.booking.screens.results.courses.CoursesListAdapter;
import com.ddscanner.booking.screens.results.dailytours.DailyToursListAdapter;
import com.ddscanner.booking.screens.results.fundives.FunDivesListAdapter;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends BaseAppCompatActivity implements DialogClosedListener {

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

    public static void show(Context context, Source source, long id) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(ARG_SOURCE, source);
        intent.putExtra(ARG_ID, id);
        context.startActivity(intent);
    }

    public enum Source {
        DAILY_TOUR, FUN_DIVE, COURSE
    }

    @BindView(R.id.progressBar)
    ProgressView progressView;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private Source source;
    private DailyToursListAdapter dailyToursListAdapter;
    private FunDivesListAdapter funDivesListAdapter;
    private CoursesListAdapter coursesListAdapter;
    private long dcId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divecenter_list);
        ButterKnife.bind(this);
        source = (Source) getIntent().getSerializableExtra(ARG_SOURCE);
        dcId = getIntent().getLongExtra(ARG_ID, -1);
        setupUi();
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
