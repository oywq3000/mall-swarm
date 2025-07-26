package com.oyproj.portal.service;

import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.model.PmsBrand;
import com.oyproj.mall.model.PmsProduct;

import java.util.List;

/**
 * @author oy
 * @description 前台品牌管理Service
 */
public interface PortalBrandService {
    /**
     * 分页获取推荐品牌
     */
    IPageInfo<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * 获取品牌详情
     */
    PmsBrand detail(Long brandId);

    /**
     * 分页获取品牌关联商品
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
