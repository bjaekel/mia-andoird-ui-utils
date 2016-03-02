package de.sevenfactory.mia.modules.paging;

import java.util.List;

public interface Filter<T> {
    void filter(List<T> input);
}
