package com.oyproj.portal.dao;

import com.oyproj.mall.model.SmsCoupon;
import com.oyproj.portal.dto.CartProduct;
import com.oyproj.portal.dto.PromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author oy
 * @description 前台自定义商品Dao
 */
public interface PortalProductDao {
    CartProduct getCartProduct(@Param("id") Long id);
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId")Long productCategoryId);
}
