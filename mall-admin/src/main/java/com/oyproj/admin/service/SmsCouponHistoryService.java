package com.oyproj.admin.service;

import com.oyproj.mall.model.SmsCouponHistory;
import com.oyproj.common.api.IPageInfo;

/**
 * @author oy
 * @description 优惠卷领取记录管理Service
 */
public interface SmsCouponHistoryService {
    /**
     * 分页查序优惠卷领取记录
     * @param couponId 优惠券id
     * @param useStatus 使用状态
     * @param orderSn 使用订单号码
     * @return
     */
    IPageInfo<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
