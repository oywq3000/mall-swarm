package com.oyproj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyproj.mall.model.UmsResourceCategory;

import java.util.List;

/**
 * @author oy
 * @description 后台资源分类管理器Service
 */
public interface UmsResourceCategoryService extends IService<UmsResourceCategory> {
    /**
     * 获取所有资源分类
     */
    List<UmsResourceCategory> listAll();
    /**
     * 创建资源分类
     */
    int create(UmsResourceCategory umsResourceCategory);

    /**
     * 修改资源分类
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * 删除资源分类
     */
    int delete(Long id);
}
