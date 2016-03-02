package de.sevenfactory.mia.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;


public class PercentScrollView extends ScrollView {
    private float mScrollPercentage;

    public PercentScrollView(Context context) {
        this(context, null, 0);
    }

    public PercentScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PercentScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        if (mScrollPercentage > 0) {
            float childHeight = getChildCount() > 0 ? getChildAt(0).getHeight() : 0;
            int scroll = (int) (mScrollPercentage * childHeight);

            setScrollY(scroll);
            mScrollPercentage = 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float childHeight = getChildCount() > 0 ? getChildAt(0).getHeight() : 0;
        if (childHeight > 0) {
            float scrollY = getScrollY();
            mScrollPercentage = scrollY / childHeight;
        }
    }
}
