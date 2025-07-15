package com.oyproj.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dto.SmsFlashPromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author oy
 * @description  自定义限时购商品关系管理Dao
 */
public interface SmsFlashPromotionProductRelationDao {
    /**
     * 获取限时购及相关商品信息
     */
    Page<SmsFlashPromotionProduct> getList(Page<SmsFlashPromotionProduct> page, @Param("flashPromotionId") Long flashPromotionId, @Param("flashPromotionSessionId") Long flashPromotionSessionId);
}
