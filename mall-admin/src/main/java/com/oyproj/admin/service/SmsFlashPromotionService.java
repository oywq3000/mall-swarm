package com.oyproj.admin.service;

import com.oyproj.admin.model.SmsFlashPromotion;
import com.oyproj.common.api.IPageInfo;

import java.util.List;

/**
 * @author oy
 * @description 限时购活动管理Service
 */
public interface SmsFlashPromotionService {
    /**
     * 添加活动
     */
    int create(SmsFlashPromotion flashPromotion);

    /**
     * 修改指定活动
     */
    int update(Long id, SmsFlashPromotion flashPromotion);

    /**
     * 删除单个活动
     */
    int delete(Long id);

    /**
     * 修改上下线状态
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取详细信息
     */
    SmsFlashPromotion getItem(Long id);

    /**
     * 分页查询活动
     */
    IPageInfo<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum);
}
