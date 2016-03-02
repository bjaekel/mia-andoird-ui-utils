package de.sevenfactory.mia.modules.paging;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MockPagingRepository implements PagingRepository<MockPagingModel> {
    public static final int TOTAL_COUNT = 5;

    @Override
    public PageModel<? extends MockPagingModel> fetchItems(int skipItems, int limitItems, String... ids) {
        return getPageModel(skipItems);
    }
    
    @NonNull
    private PageModel<? extends MockPagingModel> getPageModel(int skipItems) {
        List<MockPagingModel> items = new ArrayList<>();
        items.add(new MockPagingModel(skipItems));
        
        return new MockPage<>(TOTAL_COUNT, items);
    }
}
