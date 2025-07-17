package com.oyproj.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author oy
 * @description 查询单个产品进行修改时返回的结果
 */
public class PmsProductResult extends PmsProductParam{
    @Getter
    @Setter
    @Schema(title = "商品所选分类的父id")
    private Long cateParentId;
}
