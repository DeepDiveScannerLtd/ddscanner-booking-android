package com.ddscanner.booking.models;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.utils.Helpers;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DailyTourWithDc implements Serializable {

    private long id;
    private String name;
    private String photo;
    @SerializedName("price_from")
    private String price;
    @SerializedName("number_of_dives")
    private String numberOfDives;
    private String numberOfDivesString;
    @SerializedName("level")
    private int diverLevel;
    private String divesLevelString;
    @SerializedName("dive_center")
    private DiveCenterShort diveCenter;

    public int getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(int diverLevel) {
        this.diverLevel = diverLevel;
    }

    public String getNumberOfDives() {
        return numberOfDives;
    }

    public void setNumberOfDives(String numberOfDives) {
        this.numberOfDives = numberOfDives;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
//        return "Diving to Racha Yai";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
//        return "https://pp.userapi.com/c626824/v626824069/3fae/lZ_07Lvm9MA.jpg";
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
//            return "2000 THB";
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumberOfDivesString() {
        if (numberOfDives == null) {
            numberOfDivesString = "";
        } else {
            numberOfDivesString = numberOfDives + " " + DDScannerBookingApplication.getInstance().getString(R.string.dives_pattern);
        }
        return numberOfDivesString;
    }

    public String getDivesLevelString() {
        if (numberOfDives == null) {
            return Helpers.getDiverLevel(diverLevel);
        }
        return numberOfDives + " dives, " + Helpers.getDiverLevel(diverLevel);
    }

    public DiveCenterShort getDiveCenter() {
        return diveCenter;
    }

    public void setDiveCenter(DiveCenterShort diveCenter) {
        this.diveCenter = diveCenter;
    }
}
