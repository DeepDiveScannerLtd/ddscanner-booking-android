package com.ddscanner.booking.screens.divecenter.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DiveCenterLocationActivity extends BaseAppCompatActivity {

    public static void show(Context context, LatLng latLng) {
        EventsTracker.trackAddressClicked();
        Intent intent = new Intent(context, DiveCenterLocationActivity.class);
        intent.putExtra(LATLNG, latLng);
        context.startActivity(intent);
    }

    public static final String LATLNG = "LATLNG";

    private Toolbar toolbar;
    private MapFragment mapFragment;
    private LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_spot_location);
        latLng = getIntent().getParcelableExtra(LATLNG);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ac_back);
        getSupportActionBar().setTitle(getResources().getString(R.string.dive_center));
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map_fragment);

        mapFragment.getMapAsync(googleMap -> {
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_dc)).position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
