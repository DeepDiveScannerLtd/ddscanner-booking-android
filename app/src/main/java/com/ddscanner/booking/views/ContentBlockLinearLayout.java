package com.ddscanner.booking.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.ddscanner.booking.R;

import com.ddscanner.booking.utils.Helpers;

public class ContentBlockLinearLayout extends LinearLayout {

    private Paint paint;
    private boolean isNeedToDrawTopDivider;
    private boolean isNeedToDrawBottomDivider;
    private Bitmap lineBitmap;

    public ContentBlockLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ContentBlockLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ContentBlockLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setWillNotDraw(false);
        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        lineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.divider);
        lineBitmap = Bitmap.createBitmap(lineBitmap);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context,R.color.divider_color));
        paint.setStrokeWidth(Helpers.convertDpToIntPixels(1, getContext()));
        if (attributeSet != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attributeSet, R.styleable.ContentBlockLinearLayout);
            isNeedToDrawBottomDivider = arr.getBoolean(R.styleable.ContentBlockLinearLayout_bottom_divider, true);
            isNeedToDrawTopDivider = arr.getBoolean(R.styleable.ContentBlockLinearLayout_top_divider, true);
            arr.recycle();
            invalidate();
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
//        lineBitmap.setWidth(getMeasuredWidth());
        super.onDraw(canvas);
        if (isNeedToDrawTopDivider) {
            canvas.drawLine(0, 1, getMeasuredWidth(), 1, paint);
//            canvas.drawBitmap(lineBitmap, 0, 0, null);
        }
        if (isNeedToDrawBottomDivider) {
            canvas.drawLine(0, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, paint);
//            canvas.drawBitmap(lineBitmap, 0, 0, null);
        }
    }
}
