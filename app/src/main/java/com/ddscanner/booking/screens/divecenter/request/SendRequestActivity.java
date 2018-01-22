package com.ddscanner.booking.screens.divecenter.request;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.base.BaseAppCompatActivity;
import com.ddscanner.booking.interfaces.DialogClosedListener;
import com.ddscanner.booking.models.requests.DiveCenterRequestBookingRequest;
import com.ddscanner.booking.rest.DDScannerRestClient;
import com.ddscanner.booking.screens.search.MapsActivity;
import com.ddscanner.booking.screens.search.SearchLocationActivity;
import com.ddscanner.booking.ui.dialogs.UserActionInfoDialogFragment;
import com.ddscanner.booking.utils.Helpers;
import com.ddscanner.booking.views.PhoneInputView;


public class SendRequestActivity extends BaseAppCompatActivity implements DialogClosedListener {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_SOURCE = "source";

    DDScannerRestClient.ResultListener<Void> resultListener = new DDScannerRestClient.ResultListener<Void>() {
        @Override
        public void onSuccess(Void result) {
            materialDialog.dismiss();
            EventsTracker.trackBookingRequestSent();
            UserActionInfoDialogFragment.showForActivityResult(getSupportFragmentManager(), R.string.success_booking_dialog_title, R.string.success_booking_dialog_text, 1, false);
        }

        @Override
        public void onConnectionFailure() {
            UserActionInfoDialogFragment.show(getSupportFragmentManager(), R.string.error_connection_error_title, R.string.error_connection_failed, false);
        }

        @Override
        public void onError(DDScannerRestClient.ErrorType errorType, Object errorData, String url, String errorMessage) {
            UserActionInfoDialogFragment.show(getSupportFragmentManager(), R.string.error_server_error_title, R.string.error_unexpected_error, false);
        }

        @Override
        public void onInternetConnectionClosed() {
            UserActionInfoDialogFragment.show(getSupportFragmentManager(), R.string.error_internet_connection_title, R.string.error_internet_connection, false);
        }
    };

    DiveCenterRequestInputView nameInputView;
    DiveCenterRequestInputView emailInputView;
    DiveCenterRequestInputView messageInputView;
    PhoneInputView phoneInputView;
    String diveSpotId;
    String diveCenterId;
    long courseId;
    MaterialDialog materialDialog;
    boolean isForProduct = false;
    long productId;
    RequestSource requestSource;
    private long funDiveId;

    enum RequestSource {
        PRODUCT("product_details"), NONE("none"), COURSE("course_list_item"), FUNDIVE("fun_dive");

        private String source;

        RequestSource(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }
    }

    public static void show(Context context, int diveCenterId, EventsTracker.InquiryViewSource source) {
        Intent intent = new Intent(context, SendRequestActivity.class);
        intent.putExtra("dc_id", String.valueOf(diveCenterId));
//        intent.putExtra("ds_id", diveSpotId);
        EventsTracker.trackInquiryView(source);
        intent.putExtra(ARG_SOURCE, RequestSource.NONE);
        context.startActivity(intent);
    }

    public static void showForProduct(Context context, long productId) {
        Intent intent = new Intent(context, SendRequestActivity.class);
        intent.putExtra(ARG_PRODUCT_ID, productId);
        intent.putExtra(ARG_SOURCE, RequestSource.PRODUCT);
        context.startActivity(intent);
    }

    public static void showForFunDive(Context context, long funDiveId) {
        Intent intent = new Intent(context, SendRequestActivity.class);
        intent.putExtra(ARG_PRODUCT_ID, funDiveId);
        intent.putExtra(ARG_SOURCE, RequestSource.FUNDIVE);
        context.startActivity(intent);
    }

    public static void showForCourse(Context context, long funDiveId) {
        Intent intent = new Intent(context, SendRequestActivity.class);
        intent.putExtra(ARG_PRODUCT_ID, funDiveId);
        intent.putExtra(ARG_SOURCE, RequestSource.COURSE);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        setupToolbar(R.string.send_request, R.id.toolbar, true);
        nameInputView = findViewById(R.id.name_input);
        emailInputView = findViewById(R.id.email_input);
        emailInputView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        messageInputView = findViewById(R.id.message_input);
        phoneInputView = findViewById(R.id.phone_input);
        requestSource = (RequestSource) getIntent().getSerializableExtra(ARG_SOURCE);
        switch (requestSource) {
            case NONE:
                diveCenterId = getIntent().getStringExtra("dc_id");
//                diveSpotId = getIntent().getStringExtra("ds_id");
                break;
            case FUNDIVE:
                funDiveId = getIntent().getLongExtra(ARG_PRODUCT_ID, -1);
                EventsTracker.trackInquiryView(EventsTracker.InquiryViewSource.PRODUCT_DETAILS);
                break;
            case PRODUCT:
                productId = getIntent().getLongExtra(ARG_PRODUCT_ID, -1);
                EventsTracker.trackInquiryView(EventsTracker.InquiryViewSource.PRODUCT_DETAILS);
                break;
            case COURSE:
                courseId = getIntent().getLongExtra(ARG_PRODUCT_ID, -1);
                EventsTracker.trackInquiryView(EventsTracker.InquiryViewSource.CURSE_LIST_ITEM);
                break;
        }
        materialDialog = Helpers.getMaterialDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.send_request:
                sendRequest();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventsTracker.trackBookingCancelled();
    }

    private void sendRequest() {
        if (isDataValid()) {
            materialDialog.show();
            DDScannerBookingApplication.getInstance().getDdScannerRestClient().requestBooking(centerRequestBookingRequest(), resultListener);
        }
    }

    private DiveCenterRequestBookingRequest centerRequestBookingRequest() {
        DiveCenterRequestBookingRequest diveCenterRequestBookingRequest = new DiveCenterRequestBookingRequest();
        switch (requestSource) {
            case PRODUCT:
                diveCenterRequestBookingRequest.setProducId(productId);
                break;
            case FUNDIVE:
                diveCenterRequestBookingRequest.setFunDiveId(funDiveId);
                break;
            case NONE:
                diveCenterRequestBookingRequest.setDiveCenterId(diveCenterId);
//                diveCenterRequestBookingRequest.setDiveSpotId(diveSpotId);
                break;
            case COURSE:
                diveCenterRequestBookingRequest.setCourseId(courseId);
                break;
        }

        diveCenterRequestBookingRequest.setName(nameInputView.getInputText());
        diveCenterRequestBookingRequest.setPhone(phoneInputView.getPhoneWithPlus());
        diveCenterRequestBookingRequest.setEmail(emailInputView.getInputText());
        diveCenterRequestBookingRequest.setMessage(messageInputView.getInputText());
        return diveCenterRequestBookingRequest;
    }

    private boolean isDataValid() {
        nameInputView.hideError();
        emailInputView.hideError();
        messageInputView.hideError();
        phoneInputView.hideError();
        boolean isDataValid = true;
        if (nameInputView.getInputText().trim().isEmpty()) {
            isDataValid = false;
            nameInputView.showError(R.string.name_is_required);
        }
        if (emailInputView.getInputText().trim().isEmpty()) {
            isDataValid = false;
            emailInputView.showError(R.string.email_is_required);
        } else {
            if (!Helpers.checkEmail(emailInputView.getInputText().trim())) {
                isDataValid = false;
                emailInputView.showError(R.string.email_incorrect);
            }
        }
        if (messageInputView.getInputText().trim().isEmpty()) {
            isDataValid = false;
            messageInputView.showError(R.string.message_is_required);
        }
        if (!Helpers.validCellPhone(phoneInputView.getPhoneWithPlus(), phoneInputView.getCountryName())) {
            isDataValid = false;
            phoneInputView.setError();
        }
        if (messageInputView.getInputText().length() < 10) {
            isDataValid = false;
            messageInputView.showError(R.string.message_error_length);
        }
        return isDataValid;
    }

    @Override
    public void onDialogClosed(int requestCode) {
        Intent intent  = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
