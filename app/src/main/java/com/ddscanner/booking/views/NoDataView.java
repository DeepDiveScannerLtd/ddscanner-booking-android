package com.ddscanner.booking.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddscanner.booking.R;

public class NoDataView extends RelativeLayout {

    private TextView text;

    public NoDataView(Context context) {
        super(context);
        init();
    }

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_no_data, this);
        text = findViewById(R.id.text);
    }

    public void setMessageText(String text) {
        this.text.setText(text);
    }

}
