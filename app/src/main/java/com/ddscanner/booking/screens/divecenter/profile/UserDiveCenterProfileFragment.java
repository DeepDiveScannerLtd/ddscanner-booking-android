package com.ddscanner.booking.screens.divecenter.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;

import com.ddscanner.booking.R;
import com.ddscanner.booking.databinding.FragmentDiveCenterProfileBinding;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.screens.course.CourseDetailsActivity;
import com.ddscanner.booking.screens.dailytour.TourDetailsActivity;
import com.ddscanner.booking.screens.divecenter.ListActivity;
import com.ddscanner.booking.screens.divecenter.location.DiveCenterLocationActivity;
import com.ddscanner.booking.screens.fundives.FunDiveDetailsActivity;
import com.ddscanner.booking.utils.Helpers;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class UserDiveCenterProfileFragment extends Fragment {

    private DiveCenterProfile diveCenterProfile;
    private FragmentDiveCenterProfileBinding binding;
    private LatLng diveCenterLocation = null;
    private int diveCenterType;
    private static final String ARG_TYPE = "type";
    private static final String ARG_USER = "user";
    private static final String ARG_DIVE_SPOT_ID = "divespot_id";

    public static UserDiveCenterProfileFragment newInstance(DiveCenterProfile diveCenterProfile, int diveCenterType, String diveSpotId) {
        UserDiveCenterProfileFragment userDiveCenterProfileFragment = new UserDiveCenterProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER, diveCenterProfile);
        bundle.putInt(ARG_TYPE, diveCenterType);
        bundle.putString(ARG_DIVE_SPOT_ID, diveSpotId);
        userDiveCenterProfileFragment.setArguments(bundle);
        return userDiveCenterProfileFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dive_center_profile, container, false);
        diveCenterProfile = (DiveCenterProfile) getArguments().getSerializable(ARG_USER);
        if (getArguments().getString(ARG_DIVE_SPOT_ID) != null) {
            diveCenterProfile.setForBooking(true);
            diveCenterProfile.setDiveSpotBookingId(getArguments().getString(ARG_DIVE_SPOT_ID));
        }
        binding.setDiveCenterViewModel(new DiveCenterProfileFragmentViewModel(diveCenterProfile));
        diveCenterType = getArguments().getInt(ARG_TYPE, 0);
        if (diveCenterProfile.getAddresses() != null && diveCenterProfile.getAddresses().get(0).getPosition() != null) {
            diveCenterLocation = diveCenterProfile.getAddresses().get(0).getPosition();
        }
        View v = binding.getRoot();
        binding.setHandlers(this);
        setupUi();
        return v;
    }

    private void setupUi() {
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getAbout() != null) {
            binding.about.setText(binding.getDiveCenterViewModel().getDiveCenterProfile().getAbout());
            binding.about.setVisibility(View.VISIBLE);
            checkLines();
        } else {
            binding.about.setVisibility(View.GONE);
        }
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getProducts() != null) {
            DiveCenterProfileProductsAdapter diveCenterProfileProductsAdapter = new DiveCenterProfileProductsAdapter(item -> {
                TourDetailsActivity.show(getContext(), item.getId());
            });
            diveCenterProfileProductsAdapter.setDailyTours(binding.getDiveCenterViewModel().getDiveCenterProfile().getProducts());
            binding.productList.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.productList.setNestedScrollingEnabled(false);
            binding.productList.setAdapter(diveCenterProfileProductsAdapter);
        }
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getFunDives() != null) {
            DiveCenterFunDivesAdapter diveCenterFunDivesAdapter = new DiveCenterFunDivesAdapter(item -> FunDiveDetailsActivity.show(getContext(), item.getId()));
            diveCenterFunDivesAdapter.setFunDives(binding.getDiveCenterViewModel().getDiveCenterProfile().getFunDives());
            binding.funDivesList.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.funDivesList.setNestedScrollingEnabled(false);
            binding.funDivesList.setAdapter(diveCenterFunDivesAdapter);
        }
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getCourses() != null) {
            DiveCenterCoursesAdapter diveCenterCoursesAdapter = new DiveCenterCoursesAdapter(item -> CourseDetailsActivity.show(getContext(), item.getId()));
            diveCenterCoursesAdapter.setCources(binding.getDiveCenterViewModel().getDiveCenterProfile().getCourses());
            binding.coursesList.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.coursesList.setNestedScrollingEnabled(false);
            binding.coursesList.setAdapter(diveCenterCoursesAdapter);
        }
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getAssociations() != null) {
            ArrayList<String> tags = new ArrayList<>();
            for (Integer integer : binding.getDiveCenterViewModel().getDiveCenterProfile().getAssociations()) {
                tags.add(Helpers.getAssociationByCode(integer).getName());
            }
            TagsAdapter tagsAdapter = new TagsAdapter();
            tagsAdapter.setStrings(tags);
            ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .build();
            binding.tags.addItemDecoration(new SpacingItemDecoration(Helpers.convertDpToIntPixels(2, getContext()), Helpers.convertDpToIntPixels(2, getContext())));
            binding.tags.setLayoutManager(chipsLayoutManager);
            binding.tags.setAdapter(tagsAdapter);
        }
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().isDiveShop()) {
            binding.type.setText(R.string.divecenter_type_dive_shop);
        } else {
            binding.type.setText(R.string.divecenter_type_dc);
        }

        checkLines();
    }


    public void showLanguages(View view) {
        if (binding.getDiveCenterViewModel().getDiveCenterProfile().getLanguages() != null) {
//            EventsTracker.trackDcLanguagesView();
//            DiveCenterProfileLanguagesActivity.show(String.valueOf(binding.getDiveCenterViewModel().getDiveCenterProfile().getId()), getContext());
        }
    }

    public void showAllProducts(View view) {
           ListActivity.show(getContext(), ListActivity.Source.DAILY_TOUR, binding.getDiveCenterViewModel().getDiveCenterProfile().getId());
    }

    private void checkLines() {
        boolean flag = true;
        ViewTreeObserver viewTreeObserver = binding.about.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            Layout l = binding.about.getLayout();
            if (l != null) {
                int lines = l.getLineCount();
                if (lines > 0) {
                    if (l.getEllipsisCount(lines - 1) > 0) {
                        binding.showMore.setVisibility(View.VISIBLE);
                    }
                }
            }

        });

    }

    public void showMoreClicked(View view) {
        if (binding.about.isExpanded()) {
            binding.about.collapse();
            binding.showMore.setText(R.string.show_more);
        } else {
            binding.about.expand();
            binding.showMore.setText(R.string.show_less);
        }
    }

    public void showAllFunDives(View view) {
        ListActivity.show(getContext(), ListActivity.Source.FUN_DIVE, binding.getDiveCenterViewModel().getDiveCenterProfile().getId());
    }

    public void showAllCourses(View view) {
        ListActivity.show(getContext(), ListActivity.Source.COURSE, binding.getDiveCenterViewModel().getDiveCenterProfile().getId());
    }

    public void addressClicked(View view) {
        DiveCenterLocationActivity.show(getContext(), binding.getDiveCenterViewModel().getDiveCenterProfile().getAddresses().get(0).getPosition());
    }

}
