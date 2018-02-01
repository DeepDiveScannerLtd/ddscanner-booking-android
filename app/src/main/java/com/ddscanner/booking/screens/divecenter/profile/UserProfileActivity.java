package com.ddscanner.booking.screens.divecenter.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.PhotoAuthor;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.events.EmailClickedEvent;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.divecenter.request.SendRequestActivity;
import com.ddscanner.booking.ui.dialogs.UserActionInfoDialogFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


public class UserProfileActivity extends BaseAppCompatActivity implements DialogClosedListener {

    private ProgressView progressView;
    private Toolbar toolbar;
    private String userId;
    private PhotoAuthor photoAuthor;
    private int userType;
    private boolean isDiveCenterLegacy = false;
    private static final String ARG_SOURCE = "source";
    private static final String ARG_DIVE_SPOT_ID = "divespot_id";
    private static final String ARG_TYPE = "type";
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_INQUIRY_NAME = "course_name";
    private static final String ARG_INQUIRY_ID = "course_id";
    private EventsTracker.DiveCenterProfileScreenSource source;
    private boolean isForInquiry = false;
    private long inquiryId;
    private String inquiryName;
    private LatLng diveCeneterLocation;
    private Button inquiyBtn;

    private DDScannerRestClient.ResultListener<DiveCenterProfile> diveCenterProfileResultListener = new DDScannerRestClient.ResultListener<DiveCenterProfile>() {
        @Override
        public void onSuccess(DiveCenterProfile result) {
            progressView.setVisibility(View.GONE);
            setupFragment(0, result);
            inquiyBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onConnectionFailure() {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_connection_error_title, R.string.error_connection_failed, 10, false);
        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_connection_error_title, R.string.error_connection_failed, 10, false);
        }

        @Override
        public void onInternetConnectionClosed() {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_internet_connection_title, R.string.error_internet_connection, 10, false);
        }

    };

    public static void show(Context context, String userId, int userType, String name, EventsTracker.DiveCenterProfileScreenSource source) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        EventsTracker.trackDcProfileScreenView(name, userId, source);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, userType);
        intent.putExtra(ARG_SOURCE, source);
        context.startActivity(intent);
    }

    public static void showForCourseInquiry(Context context, String userId, String name, String inquiryName, long inquiryId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        EventsTracker.trackDcProfileScreenView(name, userId, EventsTracker.DiveCenterProfileScreenSource.COURSES_LIST);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, 0);
        intent.putExtra(ARG_INQUIRY_NAME, inquiryName);
        intent.putExtra(ARG_INQUIRY_ID, inquiryId);
        intent.putExtra(ARG_SOURCE,EventsTracker.DiveCenterProfileScreenSource.COURSES_LIST);
        context.startActivity(intent);
    }
    
    public static void showForFunDiveInquiry(Context context, String userId, String name, String inquiryName, long inquiryId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        EventsTracker.trackDcProfileScreenView(name, userId, EventsTracker.DiveCenterProfileScreenSource.FUN_DIVE_LIST_ITEM);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, 0);
        intent.putExtra(ARG_INQUIRY_NAME, inquiryName);
        intent.putExtra(ARG_INQUIRY_ID, inquiryId);
        intent.putExtra(ARG_SOURCE,EventsTracker.DiveCenterProfileScreenSource.FUN_DIVE_LIST_ITEM);
        context.startActivity(intent);
    }
    
    public static void showDailyTourInquiry(Context context, String userId, String name, String inquiryName, long inquiryId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        EventsTracker.trackDcProfileScreenView(name, userId, EventsTracker.DiveCenterProfileScreenSource.DAILY_TOUR_LIST_ITEM);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, 0);
        intent.putExtra(ARG_INQUIRY_NAME, inquiryName);
        intent.putExtra(ARG_INQUIRY_ID, inquiryId);
        intent.putExtra(ARG_SOURCE,EventsTracker.DiveCenterProfileScreenSource.DAILY_TOUR_LIST_ITEM);
        context.startActivity(intent);
    }

    public static void showForBooking(Context context, String userId, int userType, String diveSpotId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, userType);
        intent.putExtra(ARG_DIVE_SPOT_ID, diveSpotId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra(ARG_USER_ID);
        userType = getIntent().getIntExtra(ARG_TYPE, 0);
        setContentView(R.layout.activity_user_profile);
        source = (EventsTracker.DiveCenterProfileScreenSource) getIntent().getSerializableExtra(ARG_SOURCE);
        progressView = findViewById(R.id.progress_view);
        setupToolbar(R.string.profile, R.id.toolbar, true);
        isForInquiry = getIntent().getLongExtra(ARG_INQUIRY_ID, -1 ) != -1;
        inquiyBtn = findViewById(R.id.inquiry_btn);
        inquiyBtn.setOnClickListener(this::inquiryClicked);
        if (isForInquiry) {
            inquiryId = getIntent().getLongExtra(ARG_INQUIRY_ID, -1 );
            inquiryName = getIntent().getStringExtra(ARG_INQUIRY_NAME);
            inquiyBtn.setText(getString(R.string.inquiry_course_pattern, inquiryName));
        }
        switch (userType) {
            case 0:
                if (getIntent().getStringExtra(ARG_DIVE_SPOT_ID) == null) {
//                    EventsTracker.trackDiveCenterView(userId, DiveCenterSearchItem.DiveCenterType.USER.getType());
                }
                DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenterInformation(userId, diveCenterProfileResultListener);
                break;
        }
    }

    @Override
    public void onDialogClosed(int requestCode) {
        finish();
    }

    private void setupFragment(int userType, Object object) {
        try {
            switch (userType) {
                case 0:
                    DiveCenterProfile diveCenterProfile = (DiveCenterProfile) object;
                    diveCeneterLocation = diveCenterProfile.getAddresses().get(0).getPosition();
                    photoAuthor = new PhotoAuthor(String.valueOf(diveCenterProfile.getId()), diveCenterProfile.getName(), diveCenterProfile.getPhoto(), 0);
                    if (!isDiveCenterLegacy) {
                        setActiveFragment(UserDiveCenterProfileFragment.newInstance(diveCenterProfile, 1, getIntent().getStringExtra(ARG_DIVE_SPOT_ID)));
                        break;
                    }
                    setActiveFragment(UserDiveCenterProfileFragment.newInstance(diveCenterProfile, 2, null));
                    break;
            }
        } catch (IllegalStateException e) {
            finish();
        }
    }

    private void setActiveFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        DDScannerBookingApplication.bus.register(this);
//        DDScannerApplication.bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DDScannerBookingApplication.bus.unregister(this);
//        DDScannerApplication.bus.unregister(this);
    }

    public void inquiryClicked(View view) {
        switch (source) {
            case COURSES_LIST:
                SendRequestActivity.showForCourse(this, inquiryId);
                break;
            case FUN_DIVE_LIST_ITEM:
                SendRequestActivity.showForFunDive(this, inquiryId);
                break;
            case DAILY_TOUR_LIST_ITEM:
                SendRequestActivity.showForProduct(this, inquiryId);
                break;
                default:
                    SendRequestActivity.show(this, Integer.parseInt(userId), EventsTracker.InquiryViewSource.DIVE_CENTER_PROFILE_INQUIRY);
                    break;
        }
    }

    @Subscribe
    public void emailClicked(EmailClickedEvent emailClickedEvent) {
        if (isForInquiry) {
            switch (source) {
                case COURSES_LIST:
                    SendRequestActivity.showForCourse(this, inquiryId);
                    break;
                case FUN_DIVE_LIST_ITEM:
                    SendRequestActivity.showForFunDive(this, inquiryId);
                    break;
                case DAILY_TOUR_LIST_ITEM:
                    SendRequestActivity.showForProduct(this, inquiryId);
                    break;
            }
        } else {
            SendRequestActivity.show(this, Integer.parseInt(userId), EventsTracker.InquiryViewSource.DIVE_CENTER_PROFILE_EMAIL);
        }
    }

    @Override
    public void onBackPressed() {
        if (source == EventsTracker.DiveCenterProfileScreenSource.MAP) {
            EventsTracker.trackMapScreenView();
        }
        finish();
    }
}
