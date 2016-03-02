package de.sevenfactory.mia.view;

import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
    private View[] mPages;

    /**
     * Use String tags to assign names to the pages supplied to the constructor.
     *
     * @param pages Views to be displayed in the pager. Titles are only displayed if a tag is found.
     */
    public ViewPagerAdapter(View... pages) {
        mPages = pages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mPages[position]);
        return mPages[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPages[position]);
    }

    @Override
    public int getCount() {
        return mPages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) mPages[position].getTag();
    }

    public View getPage(int index) {
        return mPages[index];
    }
}
