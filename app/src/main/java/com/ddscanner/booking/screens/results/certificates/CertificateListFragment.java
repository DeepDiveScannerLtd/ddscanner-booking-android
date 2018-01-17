package com.ddscanner.booking.screens.results.certificates;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseListFragment;
import com.ddscanner.booking.models.Certificate;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.divecenter.ListActivity;

import java.util.ArrayList;

public class CertificateListFragment extends BaseListFragment {

    private DDScannerRestClient.ResultListener<ArrayList<Certificate>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<Certificate>>() {
        @Override
        public void onSuccess(ArrayList<Certificate> result) {
            if (result.size() > 0) {
                showRecyclerView();
                certificateListAdapter.setcertificates(result);
            } else {
                showNoDataView(getString(R.string.there_are_no_courses));
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

    CertificateListAdapter certificateListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        certificateListAdapter = new CertificateListAdapter(item -> {
            ListActivity.show(getContext(), ListActivity.Source.CERTIFICATE_COURSS, item.getId(), item.getName());});
        list.setAdapter(certificateListAdapter);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getCertificates(resultListener);
    }
}
