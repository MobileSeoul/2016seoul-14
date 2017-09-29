package com.seoulapp.ssg.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Dongyoon on 15. 7. 28..
 */
public class SsgRatioImageView extends ImageView {
    public SsgRatioImageView(Context context) {
        super(context);
    }

    public SsgRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SsgRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SsgRatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        double height = width * 2/3;
        setMeasuredDimension(width, (int) height);
    }
}
