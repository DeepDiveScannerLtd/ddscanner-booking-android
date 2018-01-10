package com.ddscanner.booking.screens.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.MapFragmentController;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.models.requests.DiveSpotsRequestMap;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.divecenter.profile.UserProfileActivity;
import com.ddscanner.booking.screens.results.ResultsActivity;
import com.ddscanner.booking.utils.Helpers;
import com.ddscanner.booking.views.DiveCenterInfoView;
import com.ddscanner.booking.views.DiveCentersFoundView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

public class MapsActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, MapFragmentController {

    public static void show(Context context) {
        EventsTracker.trackMapScreenView(EventsTracker.MapScreenViewSource.SELECT_AREA_BTN);
        Intent intent = new Intent(context, MapsActivity.class);
        context.startActivity(intent);
    }

    DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>>() {
        @Override
        public void onSuccess(ArrayList<DiveCenterProfile> result) {
            diveSpotsClusterManagerNew.updateDiveSpots(result);
            hideProgressView();
            if (result.size() > 0) {
                diveCentersFoundView.setDiveCentersCount(result.size());
                diveCentersFoundView.setVisibility(View.VISIBLE);
            } else {
                diveCentersFoundView.setVisibility(View.GONE);
            }
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

    private GoogleMap mMap;
    @BindView(R.id.map_fragment)
    MapView mapView;
    DiveSpotsClusterManagerNew diveSpotsClusterManagerNew;
    @BindView(R.id.dive_center_info_layout)
    DiveCenterInfoView diveCenterInfoView;
    private int diveSpotInfoHeight;
    @BindView(R.id.toast)
    RelativeLayout toast;
    @BindView(R.id.request_progress)
    ProgressBar progressView;
    private String lastDcId;
    @BindView(R.id.found_view)
    DiveCentersFoundView diveCentersFoundView;
    private String lastDcName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setupToolbar("Map", R.id.toolbar, true);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        diveSpotInfoHeight = Math.round(Helpers.convertDpToPixel(93, this));
        diveCenterInfoView.hide(diveSpotInfoHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        diveSpotsClusterManagerNew = new DiveSpotsClusterManagerNew(this, mMap, this);
    }

    @Override
    public void showDiveCenterInfo(Marker marker, DiveCenterProfile diveCenterProfile) {
        diveCentersFoundView.animate().translationY(-diveSpotInfoHeight);
        diveCenterInfoView.show(diveCenterProfile, marker);
        lastDcId = diveCenterProfile.getId().toString();
        lastDcName = diveCenterProfile.getName();
    }

    @Override
    public void hideDiveCenterInfo() {
        diveCentersFoundView.animate().translationY(0);
        diveCenterInfoView.hide(diveSpotInfoHeight);
    }

    @Override
    public void showZoomMessage() {
        toast.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideZoomMessage() {
        toast.setVisibility(View.GONE);
    }

    @Override
    public void loadDiveCenters(DiveSpotsRequestMap diveSpotsRequestMap) {
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenters(resultListener, diveSpotsRequestMap);
    }

    @Override
    public void showProgressView() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressView() {
        progressView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.found_view)
    public void showDcs(View view) {
        EventsTracker.trackMapeResultViewClcked();
        ResultsActivity.show(this, mMap.getProjection().getVisibleRegion().latLngBounds);
    }

    @OnClick(R.id.dive_center_info_layout)
    public void showDcProfile(View view) {
        UserProfileActivity.show(this, lastDcId, 0, lastDcName, EventsTracker.DiveCenterProfileScreenSource.MAP);
    }

}
