package com.ddscanner.booking.screens.search;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.MapFragmentController;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.models.requests.DiveSpotsRequestMap;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.utils.Helpers;
import com.ddscanner.booking.views.DiveCenterInfoView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class MapsActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, MapFragmentController {

    DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>>() {
        @Override
        public void onSuccess(ArrayList<DiveCenterProfile> result) {
            diveSpotsClusterManagerNew.updateDiveSpots(result);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setupToolbar("Map", R.id.toolbar, false);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        diveSpotsClusterManagerNew = new DiveSpotsClusterManagerNew(this, mMap, this);
    }

    @Override
    public void showDiveCenterInfo(Marker marker, DiveCenterProfile diveCenterProfile) {
        diveCenterInfoView.show(diveCenterProfile, marker);
    }

    @Override
    public void hideDiveCenterInfo() {
        diveCenterInfoView.hide(diveSpotInfoHeight);
    }

    @Override
    public void showZoomMessage() {

    }

    @Override
    public void hideZoomMessage() {

    }

    @Override
    public void loadDiveCenters(DiveSpotsRequestMap diveSpotsRequestMap) {
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenters(resultListener, diveSpotsRequestMap);
    }

    @Override
    public void showProgressView() {

    }

    @Override
    public void hideProgressView() {

    }
}
