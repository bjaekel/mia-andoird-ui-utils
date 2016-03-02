package de.sevenfactory.mia.modules.loading;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.modules.R;

@SuppressWarnings({"unchecked", "rawtypes"}) // This module expects observer of different types
public class LoadingModule extends FrameLayout implements Module, LoadingUi {

    private LoadingPresenter mPresenter;
    private View             mSolidBar;
    private ProgressBar      mProgressBar;

    public LoadingModule(Context context) {
        this(context, null);
    }

    public LoadingModule(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.module_loading, this);

        mSolidBar = findViewById(R.id.solid_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    /* Setup */

    public void setUp(Observable... observables) {
        mPresenter = new LoadingPresenter(observables);
        mPresenter.setUi(this);
    }

    public void setTintColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
        } else {
            mProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    /* Module */

    @Override
    public void onStart() {
        mPresenter.register();
    }

    @Override
    public void onStop() {
        mPresenter.unregister();
    }

    @Override
    public String[] getNeededPermissions() {
        return null;
    }

    /* LoadingPresenter.Ui */

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSolidBar.setVisibility(View.GONE);
        } else {
            mSolidBar.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
