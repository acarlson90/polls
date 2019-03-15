package com.aaroncarlson.polls.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
    private Boolean last;

    public PagedResponse() {

    }

    public PagedResponse(List<T> content, int page, int size, Long totalElements, int totalPages, Boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

}
