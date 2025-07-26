package com.oyproj.portal.service;

import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.portal.dto.PmsPortalProductDetail;
import com.oyproj.portal.dto.PmsProductCategoryNode;

import java.util.List;

/**
 * @author oy
 * @description 前台商品管理Service
 */
public interface PmsPortalProductService {
    /**
     * 综合搜索商品
     */
    IPageInfo<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 以树形结构获取所有商品分类
     */
    List<PmsProductCategoryNode> categoryTreeList();

    /**
     * 获取前台商品详情
     */
    PmsPortalProductDetail detail(Long id);
}
