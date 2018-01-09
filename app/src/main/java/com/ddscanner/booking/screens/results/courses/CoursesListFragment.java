package com.ddscanner.booking.screens.results.courses;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseListFragment;
import com.ddscanner.booking.interfaces.RecyclerViewScrolledListener;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.course.CourseDetailsActivity;

import java.util.ArrayList;

public class CoursesListFragment extends BaseListFragment implements RecyclerViewScrolledListener {

    private DDScannerRestClient.ResultListener<ArrayList<CourseDetails>> resultListener = new DDScannerRestClient.ResultListener<ArrayList<CourseDetails>>() {
        @Override
        public void onSuccess(ArrayList<CourseDetails> result) {
            hideProgressView();
            if (result.size() < 1) {
                showNoDataView(getString(R.string.there_are_no_courses));
            } else {
                showRecyclerView();
                coursesListAdapter.setCources(result);
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

    private DDScannerRestClient.ResultListener<ArrayList<CourseDetails>> paginationResultListener = new DDScannerRestClient.ResultListener<ArrayList<CourseDetails>>() {
        @Override
        public void onSuccess(ArrayList<CourseDetails> result) {
            coursesListAdapter.addCourses(result);
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

    private CoursesListAdapter coursesListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        currentPage = 1;
        coursesListAdapter = new CoursesListAdapter(item -> CourseDetailsActivity.show(getContext(), item.getId()));
        list.setAdapter(coursesListAdapter);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getCourses(resultListener, currentPage);
        setRecyclerViewScrolledListener(this);
    }

    @Override
    public void onScrolled() {
        currentPage += 1;
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getCourses(paginationResultListener, currentPage);
    }
}
