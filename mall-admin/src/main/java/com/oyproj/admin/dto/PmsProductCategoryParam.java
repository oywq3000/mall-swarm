package com.oyproj.admin.dto;


import com.oyproj.admin.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author oy
 * @description 添加更新产品分类的参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductCategoryParam {
    @Schema(title = "父分类的编号")
    private Long parentId;
    @NotEmpty
    @Schema(title = "商品分类名称",required = true)
    private String name;
    @Schema(title = "分类单位")
    private String productUnit;
    @FlagValidator(value = {"0","1"},message = "状态只能为0或1")
    @Schema(title = "是否在导航栏显示")
    private Integer navStatus;
    @FlagValidator(value = {"0","1"},message = "状态只能为0或1")
    @Schema(title = "是否进行显示")
    private Integer showStatus;
    @Min(value = 0)
    @Schema(title = "排序")
    private Integer sort;
    @Schema(title = "图标")
    private String icon;
    @Schema(title = "关键字")
    private String keywords;
    @Schema(title = "描述")
    private String description;
    @Schema(title = "产品相关筛选属性集合")
    private List<Long> productAttributeIdList;
}
