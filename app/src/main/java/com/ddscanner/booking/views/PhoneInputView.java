package com.ddscanner.booking.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddscanner.booking.R;
import com.hbb20.CountryCodePicker;


public class PhoneInputView extends RelativeLayout {

    CountryCodePicker countryCodePicker;
    EditText editText;
    TextView textView;
    ImageView delete;

    public PhoneInputView(Context context) {
        super(context);
        init();
    }

    public PhoneInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_input_phone_layout, this);
        countryCodePicker = findViewById(R.id.ccp);
        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.phone_error);
        countryCodePicker.registerCarrierNumberEditText(editText);
        delete = findViewById(R.id.ic_delete);
    }

    public void setPhone(String phone) {
        countryCodePicker.setFullNumber(phone);
    }

    public String getPhoneWithPlus() {
        if (editText.getText().toString().trim().length() > 0) {
            return countryCodePicker.getFullNumberWithPlus();
        }
        return "";
    }

    public String getPhoneWithoutPlus() {
        if (editText.getText().toString().trim().length() > 0) {
            return countryCodePicker.getFullNumber();
        }
        return "";
    }

    public void setError() {
        textView.setVisibility(VISIBLE);
    }

    public void hideError() {
        textView.setVisibility(GONE);
    }

    public String getCountryName() {
        return countryCodePicker.getSelectedCountryNameCode();
    }

    public void showDeleteButon() {
        delete.setVisibility(VISIBLE);
    }

}
