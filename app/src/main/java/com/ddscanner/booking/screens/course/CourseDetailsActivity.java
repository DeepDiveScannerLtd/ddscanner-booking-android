package com.ddscanner.booking.screens.course;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.databinding.ActivityCourseDetailsBinding;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.CourseDetails;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.certificate.CertificateDetailsActivity;
import com.ddscanner.booking.ui.dialogs.UserActionInfoDialogFragment;


public class CourseDetailsActivity extends BaseAppCompatActivity implements DialogClosedListener {

    private DDScannerRestClient.ResultListener<CourseDetails> resultListener = new DDScannerRestClient.ResultListener<CourseDetails>() {
        @Override
        public void onSuccess(CourseDetails result) {
            binding.setViewModel(new CourseDetailsActivityViewModel(result));
            binding.progressView.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);
            binding.buttonBookNow.setVisibility(View.VISIBLE);
        }

        @Override
        public void onConnectionFailure() {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_connection_error_title, R.string.error_connection_failed, 1, false);
        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_server_error_title, R.string.error_unexpected_error, 1, false);
        }

        @Override
        public void onInternetConnectionClosed() {
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.error_internet_connection_title, R.string.error_internet_connection, 1, false);
        }
    };

    private static final String ARG_ID = "id";

    public static void show(Context context, long courseId) {
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        intent.putExtra(ARG_ID, courseId);
        context.startActivity(intent);
    }

    ActivityCourseDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_details);
        binding.setHandlers(this);
        setupToolbar(R.string.course_details, R.id.toolbar, true);
        DDScannerBookingApplication.getInstance().getDdScannerRestClient().getCourse(resultListener, getIntent().getLongExtra(ARG_ID, -1));
    }

    @Override
    public void onDialogClosed(int requestCode) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
//            case R.id.about:
//                CertificateDetailsActivity.show(this, binding.getViewModel().getCourseDetails().getCrtificateId(), binding.getViewModel().getCourseDetails().getName());
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bookNowClicked(View view) {
//        SendRequestActivity.showForCourse(this, binding.getViewModel().getCourseDetails().getId());
    }

}
