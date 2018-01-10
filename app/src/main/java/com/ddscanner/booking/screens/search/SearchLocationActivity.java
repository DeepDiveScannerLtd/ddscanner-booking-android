package com.ddscanner.booking.screens.search;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.models.FeatureSearchResponseEntity;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.rest.MapboxRestClient;
import com.ddscanner.booking.screens.results.ResultsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchLocationActivity extends BaseAppCompatActivity implements GoogleApiClient.ConnectionCallbacks,  GoogleApiClient.OnConnectionFailedListener {

    DDScannerRestClient.ResultListener<FeatureSearchResponseEntity> resultListener = new DDScannerRestClient.ResultListener<FeatureSearchResponseEntity>() {
        @Override
        public void onSuccess(FeatureSearchResponseEntity result) {
//            placesListAdapter.setPlaces(result.getFeatures());
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
    @BindView(R.id.select_area_layout)
    LinearLayout selectAreaLayout;
    @BindView(R.id.edit_text)
    EditText searchInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        EventsTracker.trackSearchScreenView();
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesListAdapter = new PlacesListAdapter(googleApiClient, this::locationChosed);
        recyclerView.setAdapter(placesListAdapter);
        setSupportActionBar(toolbar);
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        try {
            getSupportActionBar().setTitle("Select location");
        } catch (NullPointerException ignored) {

        }
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tryToSendRquest(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void tryToSendRquest(final String newText) {
        handler.removeCallbacks(sendingSearchRequestRunnable);
        sendingSearchRequestRunnable = () -> {
            if (!newText.isEmpty()) {
                progressView.setVisibility(View.VISIBLE);
                    placeList = new ArrayList<String>();
                    Places.GeoDataApi.getAutocompletePredictions(googleApiClient, newText, new LatLngBounds(new LatLng(-180, -180), new LatLng(180, 180)), null).setResultCallback(
                            autocompletePredictions -> {
                                if (autocompletePredictions.getStatus().isSuccess()) {
                                    for (AutocompletePrediction prediction : autocompletePredictions) {
                                        placeList.add(prediction.getPlaceId());

                                    }
                                    progressView.setVisibility(View.GONE);
                                    placesListAdapter.setPlaces(placeList);
                                }
                            });

            }
        };
        handler.postDelayed(sendingSearchRequestRunnable, 630);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
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

    @OnClick(R.id.select_area_layout)
    public void onMapButtonClicked(View view) {
        MapsActivity.show(this);
    }

    private void locationChosed(String id) {
        Places.GeoDataApi.getPlaceById(googleApiClient, id).setResultCallback(places -> {
            if (places.getStatus().isSuccess()) {
                try {
                    Place place = places.get(0);
                    if (place.getViewport() != null) {
                        ResultsActivity.show(SearchLocationActivity.this, place.getViewport());
                    } else {
                        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(place.getLatLng().latitude - 0.2, place.getLatLng().longitude - 0.2), new LatLng(place.getLatLng().latitude + 0.2, place.getLatLng().longitude + 0.2) );
                        ResultsActivity.show(SearchLocationActivity.this, latLngBounds);
                    }
                    // placeList.add(place);
                } catch (IllegalStateException ignored) {

                }
            }
            places.release();
        });
    }

}
