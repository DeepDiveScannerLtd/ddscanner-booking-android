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
import com.ddscanner.booking.models.FunDiveDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.fundives.FunDiveDetailsActivity;

import java.util.ArrayList;

public class FunDivesListFragment extends Fragment {

    public RecyclerView list;

    private DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<FunDiveDetails>>() {
        @Override
        public void onSuccess(ArrayList<FunDiveDetails> result) {
            funDivesListAdapter.setFunDives(result);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_list, container, false);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        funDivesListAdapter = new FunDivesListAdapter(item -> FunDiveDetailsActivity.show(getContext(), item.getId()));
        list.setAdapter(funDivesListAdapter);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getFunDives(resultListener, 1);
    }
}
