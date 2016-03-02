package de.sevenfactory.mia.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.sevenfactory.mia.modules.R;

/**
 * {@link android.support.design.widget.TabLayout} doesn't handle text sizes well.
 * This very simple implementation does the job for our requirements (no indicator bars needed etc.).
 */
public class VariablySizedTabLayout extends LinearLayout {
    private ColorStateList mTabTextColors;
    @StyleRes
    private int            mTabTextAppearance;
    private int            mLastPosition;
    private boolean        mTabAllCaps;
    private boolean        mFixedSize;

    public VariablySizedTabLayout(Context context) {
        super(context, null, 0);
    }

    public VariablySizedTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VariablySizedTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VariablySizedTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VariablySizedTabLayout,
                defStyleAttr, 0);
        mTabTextAppearance = a.getResourceId(R.styleable.VariablySizedTabLayout_varTabTextAppearance, 0);

        final TypedArray ta = context.obtainStyledAttributes(mTabTextAppearance,
                R.styleable.TextAppearance);
        try {
            mTabTextColors = ta.getColorStateList(R.styleable.TextAppearance_android_textColor);
        } finally {
            ta.recycle();
        }

        if (a.hasValue(R.styleable.VariablySizedTabLayout_varTabTextColor)) {
            mTabTextColors = a.getColorStateList(R.styleable.VariablySizedTabLayout_varTabTextColor);
        }

        if (a.hasValue(R.styleable.VariablySizedTabLayout_varTabSelectedTextColor)) {
            final int selected = a.getColor(R.styleable.VariablySizedTabLayout_varTabSelectedTextColor, 0);
            mTabTextColors = createColorStateList(mTabTextColors.getDefaultColor(), selected);
        }
        mTabAllCaps = a.getBoolean(R.styleable.VariablySizedTabLayout_varTabTextAllCaps, false);

        mFixedSize = a.getBoolean(R.styleable.VariablySizedTabLayout_varTabFixedSize, false);

        a.recycle();
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[]   colors = new int[2];
        int           i      = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressWarnings("deprecation")
    public void setupWithViewPager(final ViewPager pager) {
        PagerAdapter adapter = pager.getAdapter();
        removeAllViews();
        setOrientation(HORIZONTAL);
        for (int i = 0; i < adapter.getCount(); i++) {
            CharSequence title = adapter.getPageTitle(i);
            TextView tv = new TextView(getContext());
            tv.setText(title);
            if (mTabTextAppearance > 0) {
                if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
                    tv.setTextAppearance(getContext(), mTabTextAppearance);
                } else {
                    tv.setTextAppearance(mTabTextAppearance);
                }
            }
            if (mTabTextColors != null) {
                tv.setTextColor(mTabTextColors);
            }
            tv.setAllCaps(mTabAllCaps);

            int weight;
            if (mFixedSize) {
                // Override variable tab size if xml attribute was set
                weight = 1;
            } else {
                Rect bounds = new Rect();
                tv.getPaint().getTextBounds(tv.getText().toString(), 0, tv.getText().length(), bounds);
                weight = bounds.width();
            }
            LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, weight);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(lp);
            final int index = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(index, true);
                }
            });
            addView(tv);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLastPosition > -1) {
                    getChildAt(mLastPosition).setSelected(false);
                }
                getChildAt(position).setSelected(true);
                mLastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (getChildCount() > 0) {
            getChildAt(pager.getCurrentItem()).setSelected(true);
        }
    }

    /**
     * Must be called before {@link #setupWithViewPager(ViewPager)}!
     * @param tabTextAppearance
     */
    public void setTextAppearance(int tabTextAppearance) {
        mTabTextAppearance = tabTextAppearance;
    }

    /**
     * Must be called before {@link #setupWithViewPager(ViewPager)}!
     * @param tabTextColor
     * @param tabTextColorSelected
     */
    public void setTextColors(int tabTextColor, int tabTextColorSelected) {
        mTabTextColors = createColorStateList(tabTextColor, tabTextColorSelected);
    }

    /**
     * Must be called before {@link #setupWithViewPager(ViewPager)}!
     * @param tabAllCaps
     */
    public void setAllCaps(boolean tabAllCaps) {
        mTabAllCaps = tabAllCaps;
    }

    /**
     * Must be called before {@link #setupWithViewPager(ViewPager)}!
     * @param fixedSize
     */
    public void setFixedSize(boolean fixedSize) {
        mFixedSize = fixedSize;
    }
}
