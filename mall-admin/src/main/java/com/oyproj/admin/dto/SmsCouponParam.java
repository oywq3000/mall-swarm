package com.oyproj.admin.dto;

import com.oyproj.admin.model.SmsCoupon;
import com.oyproj.admin.model.SmsCouponProductCategoryRelation;
import com.oyproj.admin.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SmsCouponParam extends SmsCoupon {
    @Schema(title = "优惠券绑定的商品")
    private List<SmsCouponProductRelation> productRelationList;
    @Schema(title = "优惠券绑定的商品分类")
    private List<SmsCouponProductCategoryRelation> productCategoryRelationList;

}
