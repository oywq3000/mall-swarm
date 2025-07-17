package com.oyproj.admin.dto;

import com.oyproj.admin.model.PmsProductAttribute;
import com.oyproj.admin.model.PmsProductAttributeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author oy
 * @description 包含有分类下属性的dto
 */
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {
    @Getter
    @Setter
    @Schema(title = "商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
}
