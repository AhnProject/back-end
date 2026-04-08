package com.devahn.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이징 응답 래퍼
 */
@Getter
@Setter
public class PageResponse<T> {
    private java.util.List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageResponse(java.util.List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < (totalPages - 1);
        this.hasPrevious = page > 0;
    }
}
