package de.sevenfactory.mia.modules.paging;

import java.util.List;

public class MockPage<T> implements PageModel<T> {
    private int     mTotalCount;
    private List<T> mItems;
    
    public MockPage(int totalCount, List<T> items) {
        mTotalCount = totalCount;
        mItems = items;
    }

    public List<T> getItems() {
        return mItems;
    }

    public int getTotalCount() {
        return mTotalCount;
    }
}
