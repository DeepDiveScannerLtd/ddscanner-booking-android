package com.ddscanner.booking.screens.certificate.courses;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.databinding.ItemDiveCenterCertificateBinding;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.screens.course.CourseDetailsActivity;
import com.ddscanner.booking.screens.divecenter.profile.UserProfileActivity;
import com.ddscanner.booking.screens.divecenter.request.SendRequestActivity;

import java.util.ArrayList;

public class CertificateCoursesAdapter extends RecyclerView.Adapter<CertificateCoursesAdapter.CertificateCourseItemViewHolder> {

    private ArrayList<CourseDetails> courseDetails = new ArrayList<>();

    public void setCourseDetails(ArrayList<CourseDetails> courseDetails) {
        this.courseDetails = courseDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseDetails.size();
    }

    @Override
    public void onBindViewHolder(CertificateCourseItemViewHolder holder, int position) {
        holder.binding.setViewModel(new CertificateCourseItemViewModel(courseDetails.get(position)));
    }

    @Override
    public CertificateCourseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDiveCenterCertificateBinding itemDiveCenterCertificateBinding = ItemDiveCenterCertificateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CertificateCourseItemViewHolder(itemDiveCenterCertificateBinding.getRoot());
    }

    class CertificateCourseItemViewHolder extends RecyclerView.ViewHolder {

        ItemDiveCenterCertificateBinding binding;

        public CertificateCourseItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(v -> UserProfileActivity.show(itemView.getContext(), courseDetails.get(getAdapterPosition()).getDiveCenterProfile().getId().toString(), 0, courseDetails.get(getAdapterPosition()).getDiveCenterProfile().getName(), EventsTracker.DiveCenterProfileScreenSource.PRODUCT_DETAILS));
            binding.inquiryBtn.setOnClickListener(v -> SendRequestActivity.showForCourse(itemView.getContext(), courseDetails.get(getAdapterPosition()).getId()));
            binding.deatilsBtn.setOnClickListener(v -> CourseDetailsActivity.show(itemView.getContext(), courseDetails.get(getAdapterPosition()).getId()));
        }
    }


}
