package com.example.realestate.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class FixedSizeViewPager extends ViewPager {
    public FixedSizeViewPager(Context context) {
        super(context);
    }

    public FixedSizeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        View child = null;
        int childHeight = 0;

        for(int i = 0, c = getChildCount(); i < c; i++) {
            child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            childHeight = child.getMeasuredHeight();
            if(childHeight > height) height = childHeight;
        }

        //height = 294;

        if (height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
