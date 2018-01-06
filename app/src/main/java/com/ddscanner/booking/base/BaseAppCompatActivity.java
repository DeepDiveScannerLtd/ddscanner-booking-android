package com.ddscanner.booking.base;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ddscanner.booking.R;

public class BaseAppCompatActivity extends AppCompatActivity {

    public void setupToolbar(int titleresId, int toolbarId, boolean isNeedShowBackButton) {
        setSupportActionBar((Toolbar) findViewById(toolbarId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(isNeedShowBackButton);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ac_back);
        getSupportActionBar().setTitle(titleresId);
    }

    public void setupToolbar(String title, int toolbarId, boolean isNeedShowBackButton) {
        setSupportActionBar((Toolbar) findViewById(toolbarId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(isNeedShowBackButton);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ac_back);
        getSupportActionBar().setTitle(title);
    }



}
