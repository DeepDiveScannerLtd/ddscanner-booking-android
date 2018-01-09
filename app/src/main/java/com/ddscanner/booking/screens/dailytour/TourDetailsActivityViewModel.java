package com.ddscanner.booking.screens.dailytour;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;


import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.models.DailyTourDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TourDetailsActivityViewModel {

    private DailyTourDetails dailyTourDetails;

    public TourDetailsActivityViewModel(DailyTourDetails dailyTourDetails) {
        this.dailyTourDetails = dailyTourDetails;
    }

    public DailyTourDetails getDailyTourDetails() {
        return dailyTourDetails;
    }

    @BindingAdapter("loadMainPhotoFrom")
    public static void loadMainPhoto(ImageView view, ArrayList<String> photo) {
        if (photo != null) {
            Picasso.with(view.getContext()).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url, photo.get(0), "2")).placeholder(R.drawable.gray_rectangle_without_corners).into(view);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.produc_def));
        }
    }

}
