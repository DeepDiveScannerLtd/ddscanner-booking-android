package com.ddscanner.booking.screens.results.certificates;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.ddscanner.booking.models.Certificate;

public class CertificateListItemViewModel {

    private Certificate certificate;

    public CertificateListItemViewModel(Certificate certificate) {
        this.certificate = certificate;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    @BindingAdapter("loadAssociationLogo")
    public static void loadAssociationPhoto(ImageView view, CertificateListItemViewModel viewModel) {
        if (viewModel != null) {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), viewModel.getCertificate().getResourceId()));
        }
    }

}
