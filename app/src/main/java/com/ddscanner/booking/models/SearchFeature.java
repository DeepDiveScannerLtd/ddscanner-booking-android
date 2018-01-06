package com.ddscanner.booking.models;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.annotations.SerializedName;

public class SearchFeature {

    @SerializedName("place_name")
    private String placeName;
    @SerializedName("bbox")
    private double[] coordiates;

    public double[] getCoordiates() {
        return coordiates;
    }

    public void setCoordiates(double[] coordiates) {
        this.coordiates = coordiates;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public LatLngBounds getBounds() {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(coordiates[1], coordiates[0]), new LatLng(coordiates[3], coordiates[2]));
        return latLngBounds;
    }

}
