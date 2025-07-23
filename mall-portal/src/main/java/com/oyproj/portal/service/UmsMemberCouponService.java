package com.oyproj.portal.service;

import com.oyproj.admin.model.SmsCoupon;
import com.oyproj.admin.model.SmsCouponHistory;
import com.oyproj.portal.dto.CartPromotionItem;
import com.oyproj.portal.dto.SmsCouponHistoryDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author oy
 * @description 用户优惠卷管理
 */
public interface UmsMemberCouponService {
    /**
     * 会员添加优惠卷
     */
    @Transactional
    void add(Long couponId);

    /**
     * 获取优惠卷历史表
     */
    List<SmsCouponHistory> listHistory(Integer useStatus);
    /**
     * 根据购物车信息获取可用优惠券
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);
    /**
     * 获取当前商品相关优惠券
     */
    List<SmsCoupon> listByProduct(Long productId);
    /**
     * 获取用户优惠券列表
     */
    List<SmsCoupon> list(Integer useStatus);

}
