package com.ddscanner.booking.models.requests;


import com.google.gson.annotations.SerializedName;

public class DiveCenterRequestBookingRequest {
    @SerializedName("dive_center_id")
    private String diveCenterId;
    @SerializedName("dive_spot_id")
    private String diveSpotId;
    @SerializedName("user_name")
    private String name;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_email")
    private String email;
    @SerializedName("message")
    private String message;
    @SerializedName("user_phone")
    private String phone;
    @SerializedName("daily_tour_id")
    private Long producId;
    @SerializedName("fundive_id")
    private Long funDiveId;
    @SerializedName("course_id")
    private Long courseId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getFunDiveId() {
        return funDiveId;
    }

    public void setFunDiveId(Long funDiveId) {
        this.funDiveId = funDiveId;
    }

    public Long getProducId() {
        return producId;
    }

    public void setProducId(Long producId) {
        this.producId = producId;
    }

    public String getDiveCenterId() {
        return diveCenterId;
    }

    public void setDiveCenterId(String diveCenterId) {
        this.diveCenterId = diveCenterId;
    }

    public String getDiveSpotId() {
        return diveSpotId;
    }

    public void setDiveSpotId(String diveSpotId) {
        this.diveSpotId = diveSpotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
