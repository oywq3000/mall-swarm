package com.oyproj.admin.service;

import com.oyproj.admin.model.PmsSkuStock;

import java.util.List;

/**
 * @author oy
 * @description sku商品库存管理Service
 */
public interface PmsSkuStockService {
    /**
     * 根据产品id和skuCode模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
