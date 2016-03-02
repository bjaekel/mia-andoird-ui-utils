package de.sevenfactory.mia.modules.irregulargrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.modules.R;
import de.sevenfactory.mia.widget.IrregularGridLayout;

public class IrregularGridModule<T> extends LinearLayout implements Module, IrregularGridUi<T> {

    private static final int NUM_ITEMS = 6;

    private IrregularGridItemFactory.OnItemClickedListener mOnItemClickedListener;
    private String                                         mTitle;
    private String                                         mButtonText;
    private boolean                                        mUsePlaceholder;
    private Button                                         mExtraButton;

    private IrregularGridLayout mGridLayout;

    private de.sevenfactory.mia.modules.irregulargrid.IrregularGridPresenter<T> mPresenter;
    private IrregularGridItemFactory<T>                                         mSevenTVIrregularGridItemFactory;

    public IrregularGridModule(Context context) {
        this(context, null);
    }

    public IrregularGridModule(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IrregularGridModule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IrregularGridModule, defStyleAttr, 0);
        mTitle = a.getString(R.styleable.IrregularGridModule_grid_title);
        mUsePlaceholder = a.getBoolean(R.styleable.IrregularGridModule_use_placeholder, false);
        mButtonText = a.getString(R.styleable.IrregularGridModule_extra_button_text);
        a.recycle();
    }

    public void setUp(IrregularGridInteractor<T> interactor,
                      Observable<List<T>> observable,
                      IrregularGridItemFactory<T> sevenTVIrregularGridItemFactory) {
        mPresenter = new IrregularGridPresenter<T>(interactor, observable, this);

        mSevenTVIrregularGridItemFactory = sevenTVIrregularGridItemFactory;

        if (mUsePlaceholder) {
            populateWithPlaceholders();
            setExtraButtonVisibility(true);
        }
    }

    public void setOnItemClickedListener(IrregularGridItemFactory.OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

    public void setOnExtraButtonClickedListener(final OnExtraButtonClickedListener listener) {
        if (listener != null) {
            mExtraButton.setVisibility(View.VISIBLE);
            mExtraButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onExtraButtonClick();
                }
            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.module_feed_item, this, true);
        setOrientation(VERTICAL);
        mGridLayout = (IrregularGridLayout) findViewById(R.id.grid);
        ((TextView) findViewById(R.id.title)).setText(mTitle);
        mExtraButton = (Button) findViewById(R.id.extra_button);
        mExtraButton.setText(mButtonText);

        if (mUsePlaceholder) {
            populateWithPlaceholders();
            setExtraButtonVisibility(true);
        }
    }

    private void setExtraButtonVisibility(boolean visible) {
        final int visibility;
        if (mButtonText == null) {
            visibility = View.GONE;
        } else if (visible) {
            visibility = View.VISIBLE;
        } else if (mUsePlaceholder) {
            visibility = View.INVISIBLE;
        } else {
            visibility = View.GONE;
        }

        mExtraButton.setVisibility(visibility);
    }

    private void populateWithPlaceholders() {
        List<T> dummyList = new ArrayList<>(NUM_ITEMS);
        for (int i = 0; i < NUM_ITEMS + 1; i++) {
            dummyList.add(null);
        }
        setItems(dummyList);
    }

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mGridLayout.adjustGrid(w, oldw, getPaddingLeft() - getPaddingRight(), true);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setItems(List<T> items) {
        if (mGridLayout == null ||
                mSevenTVIrregularGridItemFactory == null) {
            return;
        }
        setExtraButtonVisibility(items.size() > NUM_ITEMS);

        if (!items.isEmpty()) {
            mGridLayout.removeAllViews();

            for (int i = 0; i < Math.min(items.size(), NUM_ITEMS); i++) {
                final T item     = items.get(i);
                View                     itemView = mSevenTVIrregularGridItemFactory.createIrregularGridItem(getContext(), item, mOnItemClickedListener);
                mGridLayout.addView(itemView);
            }
            mGridLayout.adjustGrid(getWidth(), 0, getPaddingLeft() - getPaddingRight(), false);
        }
    }

    public interface OnExtraButtonClickedListener {
        void onExtraButtonClick();
    }

    @Override
    public String[] getNeededPermissions() {
        return null;
    }
}
