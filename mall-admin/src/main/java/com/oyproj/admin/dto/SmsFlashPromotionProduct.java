package com.oyproj.admin.dto;

import com.oyproj.admin.model.PmsProduct;
import com.oyproj.admin.model.SmsFlashPromotionProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author oy
 * @description 限时购及商品信息封装
 */
public class SmsFlashPromotionProduct extends SmsFlashPromotionProductRelation {
    @Getter
    @Setter
    @Schema(title = "关联商品")
    private PmsProduct product;
}
