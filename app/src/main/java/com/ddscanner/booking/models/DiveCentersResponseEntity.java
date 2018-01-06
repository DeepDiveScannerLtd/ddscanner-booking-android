package com.ddscanner.booking.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DiveCentersResponseEntity {

    @SerializedName("dive_centers")
    private ArrayList<DiveCenterProfile> diveCenters;

    public ArrayList<DiveCenterProfile> getDiveCenters() {
        return diveCenters;
    }

    public void setDiveCenters(ArrayList<DiveCenterProfile> diveCenters) {
        this.diveCenters = diveCenters;
    }
}
