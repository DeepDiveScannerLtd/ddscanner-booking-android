package com.ddscanner.booking.screens.search;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.models.FeatureSearchResponseEntity;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.rest.MapboxRestClient;
import com.ddscanner.booking.screens.results.ResultsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchLocationActivity extends BaseAppCompatActivity implements SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks,  GoogleApiClient.OnConnectionFailedListener {

    DDScannerRestClient.ResultListener<FeatureSearchResponseEntity> resultListener = new DDScannerRestClient.ResultListener<FeatureSearchResponseEntity>() {
        @Override
        public void onSuccess(FeatureSearchResponseEntity result) {
            placesListAdapter.setPlaces(result.getFeatures());
            progressView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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

    @BindView(R.id.places_list_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_view)
    ProgressView progressView;
    private GoogleApiClient googleApiClient;
    private PlacesListAdapter placesListAdapter;
    private Handler handler = new Handler();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Runnable sendingSearchRequestRunnable;
    private ArrayList<String> placeList = new ArrayList<>();
    MapboxRestClient mapboxRestClient =new MapboxRestClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesListAdapter = new PlacesListAdapter(googleApiClient, item -> {
            ResultsActivity.show(this, item.getBounds());});
        recyclerView.setAdapter(placesListAdapter);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ac_back);
            getSupportActionBar().setTitle(getString(R.string.search));
        } catch (NullPointerException e) {

        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        tryToSendRquest(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void tryToSendRquest(final String newText) {
        handler.removeCallbacks(sendingSearchRequestRunnable);
        sendingSearchRequestRunnable = () -> {
            if (!newText.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
                mapboxRestClient.getArrayListOfFeatures(resultListener, newText);
            }
        };
        handler.postDelayed(sendingSearchRequestRunnable, 630);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_sealife, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("321", "432");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("321", "432");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("321", "432");
    }
}
