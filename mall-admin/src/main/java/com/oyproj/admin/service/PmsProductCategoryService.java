package com.oyproj.admin.service;

import com.oyproj.admin.dto.PmsProductCategoryParam;
import com.oyproj.admin.dto.PmsProductCategoryWithChildrenItem;
import com.oyproj.admin.model.PmsProductCategory;
import com.oyproj.common.api.IPageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author oy
 * @description 商品分类Service
 */
public interface PmsProductCategoryService {
    /**
     * 创建商品分类
     */
    @Transactional
    int create(PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 修改商品分类
     */
    @Transactional
    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 分页获取商品分类
     */
    IPageInfo<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 删除商品分类
     */
    int delete(Long id);

    /**
     * 根据ID获取商品分类
     */
    PmsProductCategory getItem(Long id);

    /**
     * 批量修改导航状态
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 批量修改显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 以层级形式获取商品分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
