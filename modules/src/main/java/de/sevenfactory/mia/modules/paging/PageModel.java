package de.sevenfactory.mia.modules.paging;

import java.util.List;

public interface PageModel<T> {
    List<T> getItems();

    int getTotalCount();
}
