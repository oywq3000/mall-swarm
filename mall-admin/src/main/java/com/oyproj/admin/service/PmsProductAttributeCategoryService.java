package com.oyproj.admin.service;

import com.oyproj.admin.dto.PmsProductAttributeCategoryItem;
import com.oyproj.admin.model.PmsProductAttributeCategory;
import com.oyproj.common.api.IPageInfo;

import java.util.List;

/**
 * @author oy
 * @description 商品属性分类Service
 */
public interface PmsProductAttributeCategoryService {
    /**
     * 创建属性分类
     */
    int create(String name);

    /**
     * 修改属性分类
     */
    int update(Long id, String name);

    /**
     * 删除属性分类
     */
    int delete(Long id);

    /**
     * 获取属性分类详情
     */
    PmsProductAttributeCategory getItem(Long id);

    /**
     * 分页查询属性分类
     */
    IPageInfo<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);

    /**
     * 获取包含属性的属性分类
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
