package com.ddscanner.booking.screens.results.fundives;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.models.FunDive;
import com.squareup.picasso.Picasso;

public class FunDiveListItemViewModel {

    private FunDive funDive;

    public FunDiveListItemViewModel(FunDive funDive) {
        this.funDive = funDive;
    }

    public FunDive getFunDive() {
        return funDive;
    }

    @BindingAdapter("loadPhotoFrom")
    public static void loadFunDivePhotoFrom(ImageView view, String photo) {
        if (photo != null) {
            view.setVisibility(View.VISIBLE);
            Picasso.with(view.getContext()).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url, photo, "2")).placeholder(R.drawable.gray_rectangle_without_corners).into(view);
        } else {
            view.setVisibility(View.GONE);
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.produc_def));
        }
    }

}
