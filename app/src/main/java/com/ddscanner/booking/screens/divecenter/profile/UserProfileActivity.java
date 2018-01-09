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

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.PhotoAuthor;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.ui.dialogs.UserActionInfoDialogFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.squareup.otto.Subscribe;


public class UserProfileActivity extends BaseAppCompatActivity implements DialogClosedListener {

    private ProgressView progressView;
    private Toolbar toolbar;
    private String userId;
    private PhotoAuthor photoAuthor;
    private int userType;
    private boolean isDiveCenterLegacy = false;
    private static final String ARG_DIVE_SPOT_ID = "divespot_id";
    private static final String ARG_TYPE = "type";
    private static final String ARG_USER_ID = "user_id";
    private LatLng diveCeneterLocation;


    private DDScannerRestClient.ResultListener<DiveCenterProfile> diveCenterProfileResultListener = new DDScannerRestClient.ResultListener<DiveCenterProfile>() {
        @Override
        public void onSuccess(DiveCenterProfile result) {
            progressView.setVisibility(View.GONE);
            setupFragment(0, result);
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

    public static void show(Context context, String userId, int userType) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(ARG_TYPE, userType);
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
        progressView = findViewById(R.id.progress_view);
        setupToolbar(R.string.profile, R.id.toolbar, true);
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
//        DDScannerApplication.bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        DDScannerApplication.bus.unregister(this);
    }

}
