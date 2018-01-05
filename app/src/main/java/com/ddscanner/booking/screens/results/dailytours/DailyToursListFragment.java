package com.ddscanner.booking.screens.results.dailytours;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.base.BaseListFragment;
import com.ddscanner.booking.interfaces.RecyclerViewScrolledListener;
import com.ddscanner.booking.models.DailyTourDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;

import java.util.ArrayList;

public class DailyToursListFragment extends BaseListFragment implements RecyclerViewScrolledListener {

    private int currentPage;

    private DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>>() {
        @Override
        public void onSuccess(ArrayList<DailyTourDetails> result) {
            dailyToursListAdapter.setData(result);
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>> paginationResultListener = new DDScannerRestClient.ResultListener<ArrayList<DailyTourDetails>>() {
        @Override
        public void onSuccess(ArrayList<DailyTourDetails> result) {
            dailyToursListAdapter.addDailyTours(result);
            isLoading = false;
        }

        @Override
        public void onConnectionFailure() {

        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {

        }

        @Override
        public void onInternetConnectionClosed() {

        }
    };

    private DailyToursListAdapter dailyToursListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPage = 1;
        dailyToursListAdapter = new DailyToursListAdapter(item -> {});
        list.setAdapter(dailyToursListAdapter);
        setRecyclerViewScrolledListener(this);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDailyTours(resultListener, currentPage);
    }

    @Override
    public void onScrolled() {
        currentPage += 1;
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDailyTours(paginationResultListener, currentPage);
    }
}
