package de.sevenfactory.mia.modules.paging;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.modules.R;

/**
 * Generic class implementing an infinite scrolling list of items
 * This class is marked abstract so that it cannot be used directly from a layout.
 * Instead, create a new class which extends and implements a specific model type.
 */
public abstract class PagingModule<T> extends RecyclerView implements Module {
    private static final int ITEMS_BEFORE_NEXT_PAGE = 20;

    private de.sevenfactory.mia.modules.paging.PagingPresenter<T> mPresenter;
    
    private GridLayoutManager    mGridLayoutManager;
    private PagingItemDecoration mItemDecoration;
    
    private OnItemVisibilityChangeListener mItemVisibilityChangeListener;
    
    private float mMinItemWidth;
    private float mHorizontalMargin;
    private boolean mMultipleColumnsEnabled = true;
    
    public PagingModule(Context context) {
        this(context, null);
    }
    
    public PagingModule(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PagingModule(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    protected void setMultipleColumnsEnabled(boolean enabled) {
        mMultipleColumnsEnabled = enabled;
    }
    
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        mMinItemWidth = getResources().getDimension(R.dimen.horizontal_grid_min_width);

        mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(mGridLayoutManager);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagingModule, defStyle, 0);

        boolean useVerticalMarginOnEdges   = typedArray.getBoolean(R.styleable.PagingModule_useVerticalMarginOnEdges, false);
        boolean useHorizontalMarginOnEdges = typedArray.getBoolean(R.styleable.PagingModule_useHorizontalMarginOnEdges, false);
        float   verticalMargin             = typedArray.getDimension(R.styleable.PagingModule_gridVerticalMargin, 0);

        mHorizontalMargin = typedArray.getDimension(R.styleable.PagingModule_gridHorizontalMargin, 0);
        mItemDecoration = new PagingItemDecoration((int) mHorizontalMargin, (int) verticalMargin, useHorizontalMarginOnEdges, useVerticalMarginOnEdges, 1);

        typedArray.recycle();

        addItemDecoration(mItemDecoration);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        if (mMultipleColumnsEnabled) {
            int columnCount = (int) Math.ceil(w / (mMinItemWidth + mHorizontalMargin));
            mItemDecoration.setColumnCount(columnCount);
            mGridLayoutManager.setSpanCount(columnCount);

            invalidateItemDecorations();
        }
    }

    public void setItemVisibilityChangeListener(OnItemVisibilityChangeListener itemVisibilityChangeListener) {
        mItemVisibilityChangeListener = itemVisibilityChangeListener;
    }

    /* Setup Options */
    public void setUp(PagingInteractor<T> interactor, Observable<List<T>> observable, ItemFactory<T> itemFactory) {
        setUp(interactor, observable, itemFactory, null);

    }

    public void setUp(PagingInteractor<T> interactor, Observable<List<T>> observable, ItemFactory<T> itemFactory, Filter<T> filter) {
        setUp(new PagingPresenter<>(interactor, observable, itemFactory, filter));
    }

    protected void setUp(PagingPresenter<T> presenter) {
        mPresenter = presenter;
        addOnScrollListener(new PageableScrollListener());
        setAdapter(mPresenter);
    }

    /* Module */

    @Override
    public void onStart() {
        mPresenter.register();
        mPresenter.update();
    }
    
    @Override
    public void onStop() {
        mPresenter.unregister();
    }
    
    @Override
    public void smoothScrollToPosition(int position) {
        mGridLayoutManager.scrollToPositionWithOffset(position, 0);
    }
    
    /* Scroll Listener */

    private class PageableScrollListener extends OnScrollListener {
        private int mFirstItem = -1;
        private int mLastItem  = -1;

        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            int firstItem = mGridLayoutManager.findFirstVisibleItemPosition();
            int lastItem  = mGridLayoutManager.findLastVisibleItemPosition();

            if (lastItem > getAdapter().getItemCount() - ITEMS_BEFORE_NEXT_PAGE) {
                mPresenter.fetchNextPage();
            }

            if (mItemVisibilityChangeListener != null &&
                    (firstItem != mFirstItem || lastItem != mLastItem)) {
                mItemVisibilityChangeListener.onItemVisibilityChanged(firstItem, lastItem);
            }

            mFirstItem = firstItem;
            mLastItem = lastItem;
        }
    }
    
    public interface OnItemVisibilityChangeListener {
        void onItemVisibilityChanged(int firstItemPosition, int lastItemPosition);
    }
}
