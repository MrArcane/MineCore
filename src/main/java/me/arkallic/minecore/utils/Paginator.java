package me.arkallic.minecore.utils;

import java.util.Collections;
import java.util.List;

public class Paginator<T> {
    private final List<T> items;
    private final int pageSize;

    public Paginator(List<T> items, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public List<T> getPage(int pageNumber) {
        int totalPages = getTotalPages();
        if (pageNumber < 1 || pageNumber > totalPages) return Collections.emptyList();

        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, items.size());
        return items.subList(start, end);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getSize() {
        return items.size();
    }
}