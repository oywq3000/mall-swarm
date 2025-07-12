package com.oyproj.common.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class PageInfo<T> implements IPageInfo<T>{
    private long total;
    private int pages;
    private long current;
    private int size;
    private List<T> records;

    public PageInfo(long total, int pages, long current, int size, List<T> records) {
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }
}
