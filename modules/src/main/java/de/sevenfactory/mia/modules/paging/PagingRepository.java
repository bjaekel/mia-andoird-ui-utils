package de.sevenfactory.mia.modules.paging;

public interface PagingRepository<T> {
    
    PageModel<? extends T> fetchItems(int skipItems, int limitItems, String... ids);
}
