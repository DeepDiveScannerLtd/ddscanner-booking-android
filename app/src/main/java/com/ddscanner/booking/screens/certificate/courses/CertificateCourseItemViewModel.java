package com.ddscanner.booking.screens.certificate.courses;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.models.CourseDetails;
import com.squareup.picasso.Picasso;

public class CertificateCourseItemViewModel {

    private CourseDetails courseDetails;

    public CertificateCourseItemViewModel(CourseDetails courseDetails) {
        this.courseDetails = courseDetails;
    }

    public CourseDetails getCourseDetails() {
        return courseDetails;
    }

    @BindingAdapter("loadLogoFrom")
    public static void loadLogo(ImageView view, String photoId) {
        if (photoId != null) {
            Picasso.with(view.getContext()).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url,photoId,  "1")).into(view);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.avatar_dc_profile_def));
        }
    }

}
