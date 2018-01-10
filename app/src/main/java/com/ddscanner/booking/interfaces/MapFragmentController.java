package com.ddscanner.booking.interfaces;


import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.models.requests.DiveSpotsRequestMap;
import com.google.android.gms.maps.model.Marker;

public interface MapFragmentController {

    void loadDiveCenters(DiveSpotsRequestMap diveSpotsRequestMap);
    void showZoomMessage();
    void hideZoomMessage();
    void showDiveCenterInfo(Marker marker, DiveCenterProfile diveCenterProfile);
    void hideDiveCenterInfo();
    void showProgressView();
    void hideProgressView();
    void updateVisibleCount(int count);

}
