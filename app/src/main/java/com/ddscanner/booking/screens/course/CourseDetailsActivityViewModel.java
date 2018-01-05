package com.ddscanner.booking.screens.course;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.ddscanner.booking.models.CourseDetails;


public class CourseDetailsActivityViewModel {

    private CourseDetails courseDetails;

    public CourseDetailsActivityViewModel(CourseDetails courseDetails) {
        this.courseDetails = courseDetails;
    }

    public CourseDetails getCourseDetails() {
        return courseDetails;
    }

    @BindingAdapter("loadAssociationLogo")
    public static void loadAssociationPhoto(ImageView view, CourseDetailsActivityViewModel viewModel) {
        if (viewModel != null) {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), viewModel.getCourseDetails().getResourceId()));
        }
    }

}
