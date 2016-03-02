package de.sevenfactory.mia.modules.toolbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.modules.R;
import de.sevenfactory.mia.modules.loading.LoadingModule;

public class ToolbarModule extends FrameLayout implements Module {
    private Toolbar mToolbar;

    private int mHighlightColor;
    private LoadingModule mLoadingModule;
    
    public ToolbarModule(Context context) {
        super(context);
    }
    
    public ToolbarModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    
    public ToolbarModule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolbarModule(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }
    
    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToolbarModule, defStyleAttr, 0);
        mHighlightColor = a.getColor(R.styleable.ToolbarModule_highlightColor, 0);
        a.recycle();
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        LayoutInflater.from(getContext()).inflate(R.layout.module_toolbar, this, true);
        
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLoadingModule = (LoadingModule) findViewById(R.id.loading);
    
        setHighlightColor(mHighlightColor);
    }
    
    public void setTitle(@StringRes int stringRes) {
        mToolbar.setTitle(stringRes);
    }
    
    @SuppressWarnings("deprecation")
    public void setHighlightColorRes(@ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setHighlightColor(getContext().getColor(colorRes));
        } else {
            setHighlightColor(getContext().getResources().getColor(colorRes));
        }
    }
    
    public void setHighlightColor(@ColorInt int color) {
        mHighlightColor = color;
        mLoadingModule.setTintColor(color);
    }
    
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public LoadingModule getLoadingModule() {
        return mLoadingModule;
    }
    
    /* Module */
    @Override
    public void onStart() {
    }
    
    @Override
    public void onStop() {
    }

    @Override
    public String[] getNeededPermissions() {
        return null;
    }
}
