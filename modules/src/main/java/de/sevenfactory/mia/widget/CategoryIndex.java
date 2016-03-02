package de.sevenfactory.mia.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import de.sevenfactory.mia.modules.paging.PagingModule;

public abstract class CategoryIndex<T> extends ScrollView implements View.OnClickListener, PagingModule.OnItemVisibilityChangeListener {
    private CategoryIndexMap mIndexMap;
    private int              mCurrentIndex;

    private LinearLayout mLinearLayout;

    private View mLastSelectedView;

    public CategoryIndex(Context context) {
        super(context);
    }

    public CategoryIndex(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryIndex(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setVerticalScrollBarEnabled(false);

        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mLinearLayout);

        mIndexMap = new CategoryIndexMap();
        mCurrentIndex = -1;
    }

    protected void reset() {
        mIndexMap.clear();
        mLinearLayout.removeAllViews();
        mCurrentIndex = 0;
        mLastSelectedView = null;
    }

    protected void addFirstIndex(int index, View view) {
        if (mIndexMap.getFirst(index) != null) {
            throw new IllegalArgumentException("There's already a view for that index");
        }

        // Catch clicks
        view.setOnClickListener(this);

        mIndexMap.putFirst(index, view);
        mLinearLayout.addView(view);
    }

    protected void addLastIndex(int index, View view) {
        if (mIndexMap.getLast(index) != null) {
            throw new IllegalArgumentException("There's already a view for that index");
        }

        // Catch clicks
        view.setOnClickListener(this);

        mIndexMap.putLast(index, view);
    }

    @Override
    public void onClick(View v) {
        int index = mIndexMap.firstKeyAt(mIndexMap.firstIndexOfValue(v));
        selectClickedView(v);
        onItemClicked(index);
    }

    protected void setSelectedItem(int index) {
        if (index == mCurrentIndex) {
            return;
        }
        int firstIndex = mIndexMap.firstIndexOfKey(index);
        int lastIndex  = mIndexMap.lastIndexOfKey(index);
        int viewIndex  = Math.max(firstIndex, lastIndex);

        if (viewIndex >= 0) {
            selectView(viewIndex);
        }

        mCurrentIndex = index;
    }

    protected final int getLastSelectedViewIndex() {
        return getContainer().indexOfChild(mLastSelectedView);
    }

    protected final void setLastSelectedView(int index) {
        mCurrentIndex = index;
        mLastSelectedView = getContainer().getChildAt(index);
        mLastSelectedView.setSelected(true);
    }

    private void selectClickedView(View view) {
        view.setSelected(true);
        if (mLastSelectedView != null && mLastSelectedView != view) {
            mLastSelectedView.setSelected(false);
        }
        mLastSelectedView = view;
    }

    private void selectView(int index) {
        View view = mLinearLayout.getChildAt(index);
        view.setSelected(true);
        if (mLastSelectedView != null && mLastSelectedView != view) {
            mLastSelectedView.setSelected(false);
        }
        smoothScrollTo(0, view.getTop());
        mLastSelectedView = view;
    }

    protected void onItemClicked(int index) {
    }

    protected static class CategoryIndexMap {
        private SparseArray<View> mFirstIndexMap;
        private SparseArray<View> mLastIndexMap;

        private CategoryIndexMap() {
            mFirstIndexMap = new SparseArray<>();
            mLastIndexMap = new SparseArray<>();
        }

        public void clear() {
            mFirstIndexMap.clear();
            mLastIndexMap.clear();
        }

        public View getFirst(int index) {
            return mFirstIndexMap.get(index);
        }

        public View getLast(int index) {
            return mLastIndexMap.get(index);
        }

        public void putFirst(int index, View view) {
            mFirstIndexMap.put(index, view);
        }

        public void putLast(int index, View view) {
            mLastIndexMap.put(index, view);
        }

        public int firstIndexOfValue(View view) {
            return mFirstIndexMap.indexOfValue(view);
        }

        public int firstKeyAt(int index) {
            return mFirstIndexMap.keyAt(index);
        }

        public int firstIndexOfKey(int index) {
            return mFirstIndexMap.indexOfKey(index);
        }

        public int lastIndexOfKey(int index) {
            return mLastIndexMap.indexOfKey(index);
        }
    }

    protected final ViewGroup getContainer() {
        return mLinearLayout;
    }
}
