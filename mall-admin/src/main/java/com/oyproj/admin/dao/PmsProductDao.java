package com.oyproj.admin.dao;

import com.oyproj.admin.dto.PmsProductResult;
import org.apache.ibatis.annotations.Param;

/**
 * @author oy
 * @description 自定义商品管理Dao
 */
public interface PmsProductDao {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);
}
