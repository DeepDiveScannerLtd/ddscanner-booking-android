package com.ddscanner.booking.screens.divecenter.profile;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.models.Address;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.screens.divecenter.request.SendRequestActivity;
import com.ddscanner.booking.utils.EmailIntentBuilder;
import com.ddscanner.booking.utils.Helpers;
import com.ddscanner.booking.utils.PhoneCallIntentBuilder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DiveCenterProfileFragmentViewModel {

    private DiveCenterProfile diveCenterProfile;

    public DiveCenterProfileFragmentViewModel(DiveCenterProfile diveCenterProfile) {
        this.diveCenterProfile = diveCenterProfile;
    }

    public DiveCenterProfile getDiveCenterProfile() {
        return diveCenterProfile;
    }

    @BindingAdapter({"loadLanguagesFrom"})
    public static void loadLanguagesFrom(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            if (viewModel.getDiveCenterProfile().getLanguages() == null || viewModel.getDiveCenterProfile().getLanguages().size() == 0) {
                view.setText("0");
                return;
            }
            view.setText(viewModel.getDiveCenterProfile().getLanguagesString());
        }
    }

    @BindingAdapter({"loadImageFrom"})
    public static void loadProfileImage(ImageView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            Picasso.with(view.getContext()).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url, viewModel.getDiveCenterProfile().getPhoto(), "1")).placeholder(R.drawable.avatar_dc_profile_def).error(R.drawable.avatar_dc_profile_def).into(view);
        }
    }

    @BindingAdapter({"setAddressFrom"})
    public static void setAddresses(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        String outString = "";
        if (viewModel != null && viewModel.getDiveCenterProfile().getAddresses() != null) {
            for (Address address : viewModel.getDiveCenterProfile().getAddresses()) {
                outString = outString + address.getName() + " ";
            }
        }
        view.setText(outString);
    }

    @BindingAdapter({"loadAllProductsText"})
    public static void setProductsText(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            view.setText(view.getContext().getString(R.string.al_products_pattern, viewModel.getDiveCenterProfile().getProductsCount()));
        }
    }

    @BindingAdapter({"loadAllCoursesText"})
    public static void setCoursesText(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            view.setText(view.getContext().getString(R.string.al_courses_pattern, viewModel.getDiveCenterProfile().getCoursesCount()));
        }
    }

    @BindingAdapter({"loadAllFunDivesText"})
    public static void setFunDivesText(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            view.setText(view.getContext().getString(R.string.al_fun_dives_pattern, viewModel.getDiveCenterProfile().getFunDivesCount()));
        }
    }

    @BindingAdapter({"setEmails"})
    public static void setEmailsFrom(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        StringBuilder outString = new StringBuilder();
        ArrayList<Link> links = new ArrayList<>();
        if (viewModel != null && viewModel.getDiveCenterProfile().getEmails() != null) {
            for (String email : viewModel.getDiveCenterProfile().getEmails()) {
                StringBuilder append = outString.append(email);
                Link link = new Link(email);
                link.setUnderlined(false);
                link.setTextColor(ContextCompat.getColor(view.getContext(), R.color.dive_center_charachteristic_color));
                link.setOnLongClickListener(clickedText -> {
                    EmailIntentBuilder.from(view.getContext()).to(email).start();
                        EventsTracker.trackEmailLongClick();

                });
                link.setOnClickListener(clickedText -> {
                    EventsTracker.trackEmailClick();
                    EventsTracker.trackBookingDcProfileEmailClick();
//                    if (viewModel.getDiveCenterProfile().isForBooking()) {
                        SendRequestActivity.show(view.getContext(), viewModel.getDiveCenterProfile().getDiveSpotBookingId(), viewModel.getDiveCenterProfile().getId());
//                        return;
//                    }
//                    EmailIntentBuilder.from(view.getContext()).to(email).start();
                });
                links.add(link);
                if (viewModel.getDiveCenterProfile().getEmails().indexOf(email) != viewModel.getDiveCenterProfile().getEmails().size() - 1) {
                    append.append(", ");
                }
            }
        }
        view.setText(outString.toString());
        if (links.size() > 0) {
            LinkBuilder.on(view).addLinks(links).build();
        }
    }

    @BindingAdapter({"setPhones"})
    public static void setPhones(TextView view, DiveCenterProfileFragmentViewModel viewModel){
        StringBuilder outString = new StringBuilder();
        ArrayList<Link> links = new ArrayList<>();
        if (viewModel != null && viewModel.getDiveCenterProfile().getPhones() != null) {
            for (String phone : viewModel.getDiveCenterProfile().getPhones()) {
                StringBuilder append = outString.append(phone);
                Link link = new Link(phone);
                link.setUnderlined(false);
                link.setTextColor(ContextCompat.getColor(view.getContext(), R.color.dive_center_charachteristic_color));
                link.setOnClickListener(clickedText -> {
                  EventsTracker.trackPhoneClick();
                    PhoneCallIntentBuilder.from(view.getContext()).to(phone).start();
                });
                links.add(link);
                if (viewModel.getDiveCenterProfile().getPhones().indexOf(phone) != viewModel.getDiveCenterProfile().getPhones().size() - 1) {
                    append.append(", ");
                }
            }
        }

        view.setText(outString);
        if (links.size() > 0) {
            LinkBuilder.on(view).addLinks(links).build();
        }
    }

    @BindingAdapter({"loadServiceTypeFrom"})
    public static void setServiceType(TextView view, DiveCenterProfileFragmentViewModel viewModel) {
        if (viewModel != null) {
            switch (viewModel.getDiveCenterProfile().getServiceType()) {
                case COMPANY:
                    view.setText(R.string.company);
                    break;
                case RESELLER:
                    view.setText(R.string.reseller);
                    break;
            }
        }
    }

}
