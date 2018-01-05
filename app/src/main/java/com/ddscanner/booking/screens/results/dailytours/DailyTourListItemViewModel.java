package com.ddscanner.booking.screens.results.dailytours;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.models.DailyTour;
import com.squareup.picasso.Picasso;

public class DailyTourListItemViewModel {

    private DailyTour dailyTour;

    public DailyTourListItemViewModel(DailyTour dailyTour) {
        this.dailyTour = dailyTour;
    }

    public DailyTour getDailyTour() {
        return dailyTour;
    }

    @BindingAdapter("loadPhotoFrom")
    public static void loadTourPhoto(ImageView view, String photo) {
        if (photo != null) {
            Picasso.with(view.getContext()).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url, photo, "2")).placeholder(R.drawable.gray_rectangle_without_corners).into(view);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.produc_def));
        }
    }

    @BindingAdapter("loadDivesCountFrom")
    public static void loadDivesCount(TextView view, String divesCount) {
        if (divesCount != null) {
            view.setText(String.format("%s%s", divesCount, DDScannerBookingApplication.getInstance().getString(R.string.dives_pattern)));
        } else {
            view.setText(DDScannerBookingApplication.getInstance().getString(R.string.empty_string));
        }
    }

}
