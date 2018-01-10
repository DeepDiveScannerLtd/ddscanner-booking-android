package com.ddscanner.booking.base;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.R;
import com.ddscanner.booking.interfaces.RecyclerViewScrolledListener;
import com.ddscanner.booking.views.NoDataView;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;

public class BaseListFragment extends Fragment {

    public boolean isLoading = false;
    private static final int PAGE_SIZE = 10;
    public int currentPage;
    public RecyclerView list;
    public TextView noResultsFound;
    RecyclerViewScrolledListener recyclerViewScrolledListener;
    LinearLayoutManager linearLayoutManager;
    public NoDataView noDataView;
    public ProgressView progressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_list, container, false);
        list = view.findViewById(R.id.list);
        noDataView = view.findViewById(R.id.no_data_view);
        progressView = view.findViewById(R.id.progress_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(linearLayoutManager);
        return view;
    }

    public void showNoDataView(String text) {
        list.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
        noDataView.setMessageText(text);
    }

    public void hideProgressView() {
        progressView.setVisibility(View.GONE);
    }

    public void showRecyclerView() {
        hideProgressView();
        list.setVisibility(View.VISIBLE);
    }

    public void setRecyclerViewScrolledListener(RecyclerViewScrolledListener recyclerViewScrolledListener) {
        this.recyclerViewScrolledListener = recyclerViewScrolledListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initializeListenerForHighVersions();
        } else {
            initilizeListenerForLowVersions();
        }

    }

    @TargetApi(23)
    private void initializeListenerForHighVersions() {
        RecyclerView.OnScrollChangeListener listener = new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                tryingToReloadData();
            }
        };
        list.setOnScrollChangeListener(listener);
    }

    @SuppressWarnings("deprecation")
    private void initilizeListenerForLowVersions() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                tryingToReloadData();
            }
        };
        list.setOnScrollListener(scrollListener);
    }

    private void tryingToReloadData() {
        int visibleItemsCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (!isLoading) {
            if ((visibleItemsCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                recyclerViewScrolledListener.onScrolled();
                isLoading = true;
            }
        }
    }

}
