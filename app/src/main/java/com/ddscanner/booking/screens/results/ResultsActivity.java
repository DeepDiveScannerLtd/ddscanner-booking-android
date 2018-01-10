package com.ddscanner.booking.screens.results;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.screens.results.certificates.CertificateListFragment;
import com.ddscanner.booking.screens.results.courses.CoursesListFragment;
import com.ddscanner.booking.screens.results.dailytours.DailyToursListFragment;
import com.ddscanner.booking.screens.results.divecenters.DiveCentersListFragment;
import com.ddscanner.booking.screens.results.fundives.FunDivesListFragment;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsActivity extends BaseAppCompatActivity {

    private static final String ARG_BOUNDS = "bounds";
    private LatLngBounds latLngBounds;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    CertificateListFragment coursesListFragment = new CertificateListFragment();
    FunDivesListFragment funDivesListFragment = new FunDivesListFragment();
    DailyToursListFragment dailyToursListFragment = new DailyToursListFragment();
    DiveCentersListFragment diveCentersListFragment = new DiveCentersListFragment();
    ResultsPagerAdapter resultsPagerAdapter;

    public static void show(Context context, LatLngBounds latLngBounds) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(ARG_BOUNDS, latLngBounds);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        EventsTracker.trackDailyToursTabView();
        latLngBounds = (LatLngBounds) getIntent().getParcelableExtra(ARG_BOUNDS);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().setLatLngBounds(latLngBounds);
        resultsPagerAdapter = new ResultsPagerAdapter(getSupportFragmentManager());
        setupViewPager();
        setupToolbar("Search results", R.id.toolbar, true);
    }

    private void setupViewPager() {
        resultsPagerAdapter.addFragment(dailyToursListFragment, "Daily Tours");
        resultsPagerAdapter.addFragment(funDivesListFragment, "Fun Diving");
        resultsPagerAdapter.addFragment(coursesListFragment, "Courses");
        resultsPagerAdapter.addFragment(diveCentersListFragment, "Dive Centers");
        viewPager.setAdapter(resultsPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.getTabAt(3).setText("Dive Centers");
        tabLayout.getTabAt(0).setText("Daily Tours");
        tabLayout.getTabAt(1).setText("Fun Diving");
        tabLayout.getTabAt(2).setText("Courses");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        EventsTracker.trackDailyToursTabView();
                        break;
                    case 1:
                        EventsTracker.trackFunDivesTabView();
                        break;
                    case 2:
                        EventsTracker.trackCoursesTabView();
                        break;
                    case 3:
                        EventsTracker.trackDiveCentersTabView();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
