package com.ddscanner.booking.rest;


import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.models.FeatureSearchResponseEntity;
import com.ddscanner.booking.utils.Helpers;
import com.google.gson.Gson;

import org.json.JSONException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MapboxRestClient {


    protected Gson gson = new Gson();

    public void getArrayListOfFeatures(DDScannerRestClient.ResultListener<FeatureSearchResponseEntity> resultListener, String query) {
        if (!Helpers.hasConnection(DDScannerBookingApplication.getInstance())) {
            resultListener.onInternetConnectionClosed();
            return;
        }
        Call<ResponseBody> call = RestClient.getMapboxGeocodingApiRestService().getPlacesForQuesry(query, DDScannerBookingApplication.getInstance().getString(R.string.mapbox_api_key), "en");
        call.enqueue(new MapBoxResponseEntityCallback<FeatureSearchResponseEntity>(gson, resultListener) {
            @Override
            void handleResponseString(DDScannerRestClient.ResultListener<FeatureSearchResponseEntity> resultListener, String responseString) throws JSONException {
                FeatureSearchResponseEntity searchResponseEntity = gson.fromJson(responseString, FeatureSearchResponseEntity.class);
                resultListener.onSuccess(searchResponseEntity);
            }
        });
    }

}
