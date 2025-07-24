package com.oyproj.portal.service;

import com.oyproj.portal.dto.CartPromotionItem;
import com.oyproj.mall.model.OmsCartItem;

import java.util.List;

/**
 * @author oy
 * @description 促销管理Service
 */
public interface OmsPromotionService {
    /**
     * 计算购物车中的促销活动信息
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
