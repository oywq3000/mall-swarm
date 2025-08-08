package com.oyproj.portal.service;

import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.mall.model.PmsSearchHistory;

import java.util.List;

public interface PmsSearchService {
    IPageInfo<PmsProduct> search(String keyword, Integer pageNum, Integer pageSize, Integer sort);

    void storeSearchHistory(String keyword);

    List<PmsSearchHistory> getSearchHistory();
    public int dropSearchHistory(List<Long> ids);
}
