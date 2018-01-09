package com.ddscanner.booking.screens.results.divecenters;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.base.BaseListFragment;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.rest.DDScannerRestClient;

import java.util.ArrayList;

public class DiveCentersListFragment extends BaseListFragment {

    DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<DiveCenterProfile>>() {
        @Override
        public void onSuccess(ArrayList<DiveCenterProfile> result) {
            diveSpotsMapDiveCenterListAdapter.setList(result);
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

    DiveSpotsMapDiveCenterListAdapter diveSpotsMapDiveCenterListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diveSpotsMapDiveCenterListAdapter = new DiveSpotsMapDiveCenterListAdapter(getActivity());
        list.setAdapter(diveSpotsMapDiveCenterListAdapter);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getDiveCenters(resultListener, DDScannerBookingApplication.getInstance().getDdScannerRestClient().getRequestMap());
    }
}
