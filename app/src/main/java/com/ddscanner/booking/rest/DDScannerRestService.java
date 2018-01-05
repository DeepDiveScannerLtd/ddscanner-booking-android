package com.ddscanner.booking.rest;


import com.ddscanner.booking.models.requests.PaginationListRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DDScannerRestService {

    @GET("v2_6/daily_tours.get")
    Call<ResponseBody> getDailyTours(@QueryMap PaginationListRequest request);

    @GET("v2_6/courses.get")
    Call<ResponseBody> getCourses(@QueryMap PaginationListRequest request);

    @GET("v2_6/fundives.get")
    Call<ResponseBody> getunDives(@QueryMap PaginationListRequest request);

}
