package de.sevenfactory.mia.modules.feed;

import de.sevenfactory.mia.modules.irregulargrid.IrregularGridModel;
import de.sevenfactory.mia.modules.paging.PageModel;

public interface FeedRepository {
    PageModel<? extends IrregularGridModel> fetchFeedItems(int limit);
}
