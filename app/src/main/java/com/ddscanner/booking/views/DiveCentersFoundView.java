package com.ddscanner.booking.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddscanner.booking.R;

public class DiveCentersFoundView extends LinearLayout {

    private TextView textView;

    public DiveCentersFoundView(Context context) {
        super(context);
        init();
    }

    public DiveCentersFoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiveCentersFoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_dive_centers_found, this);
        textView = findViewById(R.id.dcs_count);
    }

    public void setDiveCentersCount(int count) {
        textView.setText(getContext().getString(R.string.see_all_dcs_pattern, count));
    }

}
