package com.oyproj.admin.dto;

import com.oyproj.admin.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by macro on 2018/5/25.
 */
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {
    @Getter
    @Setter
    @Schema(title = "子级分类")
    private List<PmsProductCategory> children;
}
