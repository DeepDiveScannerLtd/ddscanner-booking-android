package com.ddscanner.booking.screens.results;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.screens.results.courses.CoursesListFragment;
import com.ddscanner.booking.screens.results.dailytours.DailyToursListFragment;
import com.ddscanner.booking.screens.results.divecenters.DiveCentersListFragment;
import com.ddscanner.booking.screens.results.fundives.FunDivesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsActivity extends BaseAppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    CoursesListFragment coursesListFragment = new CoursesListFragment();
    FunDivesListFragment funDivesListFragment = new FunDivesListFragment();
    DailyToursListFragment dailyToursListFragment = new DailyToursListFragment();
    DiveCentersListFragment diveCentersListFragment = new DiveCentersListFragment();
    ResultsPagerAdapter resultsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        resultsPagerAdapter = new ResultsPagerAdapter(getSupportFragmentManager());
        setupViewPager();
        setupToolbar("Result", R.id.toolbar, true);
    }

    private void setupViewPager() {
        resultsPagerAdapter.addFragment(diveCentersListFragment, "Dive Centers");
        resultsPagerAdapter.addFragment(dailyToursListFragment, "Daily Tours");
        resultsPagerAdapter.addFragment(funDivesListFragment, "Fun Diving");
        resultsPagerAdapter.addFragment(coursesListFragment, "Courses");
        viewPager.setAdapter(resultsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.getTabAt(0).setText("Dive Centers");
        tabLayout.getTabAt(1).setText("Daily Tours");
        tabLayout.getTabAt(2).setText("Fun Diving");
        tabLayout.getTabAt(3).setText("Courses");
    }


}
