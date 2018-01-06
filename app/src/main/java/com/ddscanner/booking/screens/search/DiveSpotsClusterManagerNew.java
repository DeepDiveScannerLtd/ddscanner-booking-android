package com.ddscanner.booking.screens.search;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ddscanner.booking.R;
import com.ddscanner.booking.interfaces.MapFragmentController;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.models.requests.DiveSpotsRequestMap;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class DiveSpotsClusterManagerNew extends ClusterManager<DiveCenterProfile> implements ClusterManager.OnClusterClickListener<DiveCenterProfile>, GoogleMap.OnMapClickListener, GoogleMap.OnCameraChangeListener {


    private MapFragmentController diveSpotMapFragmentController;
    private static final String TAG = DiveSpotsClusterManagerNew.class.getName();
    private static final int CAMERA_ANIMATION_DURATION = 300;
    private final IconGenerator clusterIconGenerator1Symbol;
    private final IconGenerator clusterIconGenerator2Symbols;
    private final IconGenerator clusterIconGenerator3Symbols;
    private final FragmentActivity context;
    private GoogleMap googleMap;
    private DiveSpotsRequestMap diveSpotsRequestMap = new DiveSpotsRequestMap();
    private ArrayList<DiveCenterProfile> diveSpotShorts = new ArrayList<>();
    private HashMap<LatLng, DiveCenterProfile> itemsMap = new HashMap<>();
    private float lastZoom;

    private boolean isCanMakeRequest = false;
    private Circle circle;

    private Marker lastClickedMarker;
    private Marker userCurrentLocationMarker;
    private LatLng lastKnownSouthWest;
    private LatLng lastKnownNorthEast;
    private LatLng newDiveSpotLatLng;
    private boolean isNewDiveSpotMarkerClicked;
    private ArrayList<DiveCenterProfile> allDiveSpots = new ArrayList<>();
    private int newDiveSpotId = -1;


    public DiveSpotsClusterManagerNew(FragmentActivity context, GoogleMap googleMap, MapFragmentController diveSpotMapFragmentController) {
        super(context, googleMap);
        this.context = context;
        this.googleMap = googleMap;
        this.diveSpotMapFragmentController = diveSpotMapFragmentController;
        View clusterView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clusterIconGenerator1Symbol = new IconGenerator(context);
        clusterView = inflater.inflate(R.layout.cluster_view_1_symbol, null);
        clusterView.findViewById(R.id.cluster_label).setId(com.google.maps.android.R.id.text);
        clusterIconGenerator1Symbol.setContentView(clusterView);
        clusterIconGenerator1Symbol.setBackground(null);
        clusterIconGenerator2Symbols = new IconGenerator(context);
        clusterView = inflater.inflate(R.layout.cluster_view_2_symbols, null);
        clusterView.findViewById(R.id.cluster_label).setId(com.google.maps.android.R.id.text);
        clusterIconGenerator2Symbols.setContentView(clusterView);
        clusterIconGenerator2Symbols.setBackground(null);
        clusterIconGenerator3Symbols = new IconGenerator(context);
        clusterView = inflater.inflate(R.layout.cluster_view_3_symbols, null);
        clusterView.findViewById(R.id.cluster_label).setId(com.google.maps.android.R.id.text);
        clusterIconGenerator3Symbols.setContentView(clusterView);
        clusterIconGenerator3Symbols.setBackground(null);

        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.setOnCameraChangeListener(this);
        googleMap.setOnMarkerClickListener(this);
        setAlgorithm(new GridBasedAlgorithm<DiveCenterProfile>());
        setRenderer(new DiveSpotsClusterManagerNew.IconRenderer(context, googleMap, this));
        setOnClusterClickListener(this);
        if (checkArea(googleMap.getProjection().getVisibleRegion().latLngBounds.southwest, googleMap.getProjection().getVisibleRegion().latLngBounds.northeast)) {
            requestDiveSpots(false);
        } else {
            showToastAndHideInfo();
        }
    }

    private void showToastAndHideInfo() {
        diveSpotMapFragmentController.hideDiveCenterInfo();
        diveSpotMapFragmentController.showZoomMessage();
    }

    private void hideProgressBar() {
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(() -> diveSpotMapFragmentController.hideProgressView(), 1200);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        diveSpotMapFragmentController.hideDiveCenterInfo();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        super.onCameraChange(cameraPosition);
        if (lastZoom != googleMap.getCameraPosition().zoom && lastClickedMarker != null) {
            lastZoom = googleMap.getCameraPosition().zoom;
//            DDScannerApplication.bus.post(new OnMapClickEvent(null, false));
        }
        LatLng southwest = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest;
        LatLng northeast = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
//        parentFragment.fillDiveSpots(getVisibleMarkersList(diveSpotShorts));
        if (diveSpotsRequestMap.size() == 0) {
            diveSpotsRequestMap.putSouthWestLat(southwest.latitude - Math.abs(northeast.latitude - southwest.latitude));
            diveSpotsRequestMap.putSouthWestLng(southwest.longitude - Math.abs(northeast.longitude - southwest.longitude));
            diveSpotsRequestMap.putNorthEastLat(northeast.latitude + Math.abs(northeast.latitude - southwest.latitude));
            diveSpotsRequestMap.putNorthEastLng(northeast.longitude + Math.abs(northeast.longitude - southwest.longitude));
        }
        if (checkArea(southwest, northeast)) {
            diveSpotMapFragmentController.hideZoomMessage();
//            hideToast();
            if (isCanMakeRequest) {
                if (southwest.latitude <= diveSpotsRequestMap.getSouthWestLat() ||
                        southwest.longitude <= diveSpotsRequestMap.getSouthWestLng() ||
                        northeast.latitude >= diveSpotsRequestMap.getNorthEastLat() ||
                        northeast.longitude >= diveSpotsRequestMap.getNorthEastLng()) {
                    requestDiveSpots(false);
                }
            } else {
                requestDiveSpots(false);
            }
        } else {
            showToastAndHideInfo();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(userCurrentLocationMarker)) {
            return true;
        }
        try {
            diveSpotMapFragmentController.showDiveCenterInfo(marker, itemsMap.get(marker.getPosition()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<DiveCenterProfile> cluster) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (DiveCenterProfile baseMapEntity : cluster.getItems()) {
            builder.include(baseMapEntity.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.animateCamera(cu, CAMERA_ANIMATION_DURATION, null);
        return true;
    }

    public void updateDiveSpots(ArrayList<DiveCenterProfile> diveSpotShorts) {
        this.allDiveSpots = new ArrayList<>(diveSpotShorts);
        Log.i(TAG, "incoming dive spots size = " + diveSpotShorts.size());
        final ArrayList<DiveCenterProfile> newDiveSpotShorts = new ArrayList<>();
        newDiveSpotShorts.addAll(diveSpotShorts);
        newDiveSpotShorts.removeAll(this.diveSpotShorts);
        final ArrayList<DiveCenterProfile> deletedDiveSpotShorts = new ArrayList<>();
        deletedDiveSpotShorts.addAll(this.diveSpotShorts);
        deletedDiveSpotShorts.removeAll(diveSpotShorts);
        Log.i(TAG, "removing " + deletedDiveSpotShorts.size() + " dive spots");
        for (DiveCenterProfile baseMapEntity : deletedDiveSpotShorts) {
            removeDiveSpot(baseMapEntity);
        }
        Log.i(TAG, "adding " + newDiveSpotShorts.size() + " dive spots");
        for (DiveCenterProfile baseMapEntity : newDiveSpotShorts) {
            addNewItem(baseMapEntity);
        }
        cluster();
    }

    private void addNewItem(DiveCenterProfile baseMapEntity) {
        if (baseMapEntity.getPosition() == null) {
            Log.i(TAG, "addNewItem baseMapEntity.getPosition() == null");
        } else {
            addItem(baseMapEntity);
            itemsMap.put(baseMapEntity.getPosition(), baseMapEntity);
            diveSpotShorts.add(baseMapEntity);
        }
    }

    private void removeDiveSpot(DiveCenterProfile baseMapEntity) {
        removeItem(baseMapEntity);
        itemsMap.remove(baseMapEntity.getPosition());
        diveSpotShorts.remove(baseMapEntity);
    }

    private boolean checkArea(LatLng southWest, LatLng northEast) {
        return !(Math.abs(northEast.longitude - southWest.longitude) > 8 || Math.abs(northEast.latitude - southWest.latitude) > 8);
    }

    public void requestDiveSpots(boolean isFromFilters) {
        diveSpotsRequestMap.clear();
        LatLng southwest = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest;
        LatLng northeast = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        if (!checkArea(southwest, northeast) && isFromFilters && lastKnownNorthEast != null && lastKnownSouthWest != null) {
            southwest = lastKnownSouthWest;
            northeast = lastKnownNorthEast;
        }
        if (checkArea(southwest, northeast)) {
            sendRequest(northeast, southwest);
        }
    }

    private class IconRenderer extends DefaultClusterRenderer<DiveCenterProfile> {

        IconRenderer(Context context, GoogleMap map, ClusterManager<DiveCenterProfile> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<DiveCenterProfile> cluster, MarkerOptions markerOptions) {
            BitmapDescriptor descriptor;

            int bucket = this.getBucket(cluster);
            String clusterLabel = getClusterText(bucket);
            int symbolsCount = clusterLabel.length();
            switch (symbolsCount) {
                case 0:
                case 1:
                    descriptor = BitmapDescriptorFactory.fromBitmap(clusterIconGenerator1Symbol.makeIcon(clusterLabel));
                    break;
                case 2:
                    descriptor = BitmapDescriptorFactory.fromBitmap(clusterIconGenerator2Symbols.makeIcon(clusterLabel));
                    break;
                case 3:
                    descriptor = BitmapDescriptorFactory.fromBitmap(clusterIconGenerator3Symbols.makeIcon(clusterLabel));
                    break;
                default:
                    clusterLabel = "99+";
                    descriptor = BitmapDescriptorFactory.fromBitmap(clusterIconGenerator3Symbols.makeIcon(clusterLabel));
                    break;
            }

            markerOptions.icon(descriptor);
        }

        @Override
        protected void onClusterItemRendered(DiveCenterProfile baseMapEntity, final Marker marker) {
            super.onClusterItemRendered(baseMapEntity, marker);
            try {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_dc));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSpotVisibleOnScreen(double lat, double lng) {
        LatLng southwest = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest;
        LatLng northeast = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        return lat < northeast.latitude && lat > southwest.latitude && lng < northeast.longitude && lng > southwest.longitude;
    }

    public void mapZoomPlus() {
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void mapZoomMinus() {
        googleMap.animateCamera(CameraUpdateFactory.zoomOut());
    }


    public void setUserCurrentLocationMarker(Marker userCurrentLocationMarker) {
        this.userCurrentLocationMarker = userCurrentLocationMarker;
    }

    public Marker getLastClickedMarker() {
        return lastClickedMarker;
    }

    private void sendRequest(LatLng northeast, LatLng southwest) {
        ArrayList<String> sealifesIds = null;
        diveSpotsRequestMap.clear();
        diveSpotMapFragmentController.showProgressView();
        isCanMakeRequest = true;
        lastKnownSouthWest = southwest;
        lastKnownNorthEast = northeast;
        diveSpotsRequestMap.putSouthWestLat(southwest.latitude - Math.abs(northeast.latitude - southwest.latitude));
        diveSpotsRequestMap.putSouthWestLng(southwest.longitude - Math.abs(northeast.longitude - southwest.longitude));
        diveSpotsRequestMap.putNorthEastLat(northeast.latitude + Math.abs(northeast.latitude - southwest.latitude));
        diveSpotsRequestMap.putNorthEastLng(northeast.longitude + Math.abs(northeast.longitude - southwest.longitude));
        diveSpotMapFragmentController.loadDiveCenters(diveSpotsRequestMap);
    }


    public void moveCamera(LatLngBounds latLngBounds) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

//    public void setUserLocation(LatLng latLng) {
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(latLng)
//                .zoom(12)
//                .build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
//        if (circle == null) {
//            userCurrentLocationMarker = googleMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .anchor(0.5f, 0.5f)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_me)));
//            CircleOptions circleOptions = new CircleOptions()
//                    .center(latLng)
//                    .radius(200)
//                    .strokeColor(android.R.color.transparent)
//                    .fillColor(Color.parseColor("#1A0668a1"));
//            circle = googleMap.addCircle(circleOptions);
//        } else {
//            circle.setCenter(latLng);
//            userCurrentLocationMarker.setPosition(latLng);
//        }
//    }

}
