package com.oyproj.admin.dao;

import com.oyproj.admin.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * @author oy
 * @description 商品分类自定义Dao
 */
public interface PmsProductCategoryDao {
    /**
     * 获取商品分类及其子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
