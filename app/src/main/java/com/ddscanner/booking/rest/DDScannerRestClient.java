package com.ddscanner.booking.rest;

import android.app.Activity;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.models.Certificate;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.models.DailyTour;
import com.ddscanner.booking.models.DailyTourDetails;
import com.ddscanner.booking.models.FunDiveDetails;
import com.ddscanner.booking.models.requests.PaginationListRequest;
import com.ddscanner.booking.utils.Helpers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DDScannerRestClient {

    protected Gson gson = new Gson();

    private PaginationListRequest getPaginationListRequest(int page) {
        PaginationListRequest paginationListRequest = new PaginationListRequest();
        paginationListRequest.setKeyPage(page);
        paginationListRequest.setLimit(10);
        return paginationListRequest;
    }

    public void getCertficateDetails(ResultListener<Certificate> resultListener, long id) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }
        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getCertificateDetails(id);
        call.enqueue(new ResponseEntityCallback<Certificate>(gson, resultListener) {
            @Override
            void handleResponseString(ResultListener<Certificate> resultListener, String responseString) throws JSONException {
                resultListener.onSuccess(gson.fromJson(responseString, Certificate.class));
            }
        });
    }

    public void getFunDive(ResultListener<FunDiveDetails> resultListener, long id) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }
        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getFunDiveDetails(id);
        call.enqueue(new ResponseEntityCallback<FunDiveDetails>(gson, resultListener) {
            @Override
            void handleResponseString(ResultListener<FunDiveDetails> resultListener, String responseString) throws JSONException {
                resultListener.onSuccess(gson.fromJson(responseString, FunDiveDetails.class));
            }
        });
    }

    public void getDailyTour(ResultListener<DailyTourDetails> resultListener, long id) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }
        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getProductDetails(id);
        call.enqueue(new ResponseEntityCallback<DailyTourDetails>(gson, resultListener) {
            @Override
            void handleResponseString(ResultListener<DailyTourDetails> resultListener, String responseString) throws JSONException {
                resultListener.onSuccess(gson.fromJson(responseString, DailyTourDetails.class));
            }
        });
    }

    public void getCourse(ResultListener<CourseDetails> resultListener, long id) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }
        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getCourseDeatails(id);
        call.enqueue(new ResponseEntityCallback<CourseDetails>(gson, resultListener) {
            @Override
            void handleResponseString(ResultListener<CourseDetails> resultListener, String responseString) throws JSONException {
                resultListener.onSuccess(gson.fromJson(responseString, CourseDetails.class));
            }
        });
    }

    public void getDailyTours(ResultListener<ArrayList<DailyTourDetails>> resultListener, int page) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }

        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getDailyTours(getPaginationListRequest(page));
        call.enqueue(new ResponseEntityCallback<ArrayList<DailyTourDetails>>(gson, resultListener) {

            @Override
            void handleResponseString(ResultListener<ArrayList<DailyTourDetails>> resultListener, String responseString) throws JSONException {
                Type listType = new TypeToken<ArrayList<DailyTourDetails>>(){}.getType();
                ArrayList<DailyTourDetails> dailyTourDetails = gson.fromJson(responseString, listType);
                resultListener.onSuccess(dailyTourDetails);
            }
        });
    }

    public void getFunDives(ResultListener<ArrayList<FunDiveDetails>> resultListener, int page) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }

        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getunDives(getPaginationListRequest(page));
        call.enqueue(new ResponseEntityCallback<ArrayList<FunDiveDetails>>(gson, resultListener) {

            @Override
            void handleResponseString(ResultListener<ArrayList<FunDiveDetails>> resultListener, String responseString) throws JSONException {
                Type listType = new TypeToken<ArrayList<FunDiveDetails>>(){}.getType();
                ArrayList<FunDiveDetails> dailyTourDetails = gson.fromJson(responseString, listType);
                resultListener.onSuccess(dailyTourDetails);
            }
        });
    }

    public void getCourses(ResultListener<ArrayList<CourseDetails>> resultListener, int page) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }

        Call<ResponseBody> call = RestClient.getDdscannerServiceInstance().getCourses(getPaginationListRequest(page));
        call.enqueue(new ResponseEntityCallback<ArrayList<CourseDetails>>(gson, resultListener) {

            @Override
            void handleResponseString(ResultListener<ArrayList<CourseDetails>> resultListener, String responseString) throws JSONException {
                Type listType = new TypeToken<ArrayList<CourseDetails>>(){}.getType();
                ArrayList<CourseDetails> dailyTourDetails = gson.fromJson(responseString, listType);
                resultListener.onSuccess(dailyTourDetails);
            }
        });
    }

    public static abstract class ResultListener<T> {
        private boolean isCancelled;

        public abstract void onSuccess(T result);

        public abstract void onConnectionFailure();

        public abstract void onError(ErrorType errorType, Object errorData, String url, String errorMessage);

        public abstract void onInternetConnectionClosed();

        public boolean isCancelled() {
            return isCancelled;
        }

        public void setCancelled(boolean cancelled) {
            isCancelled = cancelled;
        }
    }

    public enum ErrorType {
        BAD_REQUEST_ERROR_400, ENTITY_NOT_FOUND_404, RIGHTS_NOT_FOUND_403, UNAUTHORIZED_401, DATA_ALREADY_EXIST_409, DIVE_SPOT_NOT_FOUND_ERROR_C802, COMMENT_NOT_FOUND_ERROR_C803, UNPROCESSABLE_ENTITY_ERROR_422, SERVER_INTERNAL_ERROR_500, IO_ERROR, JSON_SYNTAX_EXCEPTION, UNKNOWN_ERROR
    }



}
