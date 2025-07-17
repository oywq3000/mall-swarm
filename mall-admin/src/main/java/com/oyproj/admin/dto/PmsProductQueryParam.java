package com.oyproj.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author oy
 * @description 产品参数查序
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductQueryParam {
    @Schema(title = "上架状态")
    private Integer publishStatus;
    @Schema(title = "审核状态")
    private Integer verifyStatus;
    @Schema(title = "商品名称模糊关键字")
    private String keyword;
    @Schema(title = "商品货号")
    private String productSn;
    @Schema(title = "商品分类编号")
    private Long productCategoryId;
    @Schema(title = "商品品牌编号")
    private Long brandId;
}
