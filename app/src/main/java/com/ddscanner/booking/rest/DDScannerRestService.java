package com.ddscanner.booking.rest;


import com.ddscanner.booking.models.requests.DiveCenterRequestBookingRequest;
import com.ddscanner.booking.models.requests.DiveSpotsRequestMap;
import com.ddscanner.booking.models.requests.PaginationListRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DDScannerRestService {

    @GET("v2_6/daily_tours.get")
    Call<ResponseBody> getDailyTours(@QueryMap PaginationListRequest request);

    @GET("v2_6/courses.get")
    Call<ResponseBody> getCourses(@QueryMap PaginationListRequest request);

    @GET("v2_6/fundives.get")
    Call<ResponseBody> getunDives(@QueryMap PaginationListRequest request);

    @GET("v2_7/course.get")
    Call<ResponseBody> getCourseDeatails(@Query("id") long id);

    @GET("v2_7/certificate.get")
    Call<ResponseBody> getCertificateDetails(@Query("id") long id);

    @GET("v2_7/daily_tour.get")
    Call<ResponseBody> getProductDetails(@Query("id") long id);

    @GET("v2_7/fundive.get")
    Call<ResponseBody> getFunDiveDetails(@Query("id") long id);

    @GET("v2_7/map.filter")
    Call<ResponseBody> getDiveCenters(@QueryMap DiveSpotsRequestMap diveSpotsRequestMap);

    @POST("v2_7/divecenter.request_booking")
    Call<ResponseBody> postRequestBooking(@Body DiveCenterRequestBookingRequest request);

    @GET("v2_7/user.divecenter.profile.get")
    Call<ResponseBody> getDiveCenterInformation(@Query("id") String id, @Query("include_photo_details") int value);


}
