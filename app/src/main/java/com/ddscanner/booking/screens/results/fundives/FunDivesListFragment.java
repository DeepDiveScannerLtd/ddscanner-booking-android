package com.ddscanner.booking.screens.results.fundives;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseListFragment;
import com.ddscanner.booking.interfaces.RecyclerViewScrolledListener;
import com.ddscanner.booking.models.FunDiveDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.fundives.FunDiveDetailsActivity;

import java.util.ArrayList;

public class FunDivesListFragment extends BaseListFragment implements RecyclerViewScrolledListener {

    private DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>>() {
        @Override
        public void onSuccess(ArrayList<FunDiveDetails> result) {
            hideProgressView();
            if (result.size() > 0) {
                showRecyclerView();
                funDivesListAdapter.setFunDives(result);
            } else {
                showNoDataView(getString(R.string.there_are_no_fun_diving));
            }
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

    private DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>> paginationResultListener = new DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>>() {
        @Override
        public void onSuccess(ArrayList<FunDiveDetails> result) {
            funDivesListAdapter.addFunDives(result);
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

    private FunDivesListAdapter funDivesListAdapter;
    private int currentPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        currentPage = 1;
        funDivesListAdapter = new FunDivesListAdapter(item -> FunDiveDetailsActivity.show(getContext(), item.getId()));
        list.setAdapter(funDivesListAdapter);
        setRecyclerViewScrolledListener(this);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getFunDives(resultListener, currentPage);
    }

    @Override
    public void onScrolled() {
        currentPage += 1;
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getFunDives(paginationResultListener, currentPage);
    }

}
