package com.oyproj.admin.dao;

import com.oyproj.admin.dto.PmsProductAttributeCategoryItem;

import java.util.List;

/**
 * @author oy
 * @description 自定义商品属性分类Dao
 */
public interface PmsProductAttributeCategoryDao {
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
