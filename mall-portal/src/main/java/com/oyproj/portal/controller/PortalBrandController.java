package com.oyproj.portal.controller;

import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.model.PmsBrand;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.portal.service.PortalBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 首页品牌推荐管理Controller
 */
@RestController
@Tag(name = "PortalBrandController", description = "前台品牌管理")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class PortalBrandController {
    private final PortalBrandService homeBrandService;
    @Operation(summary = "分页获取推荐品牌")
    @GetMapping(value = "/recommendList")
    public CommonResult<List<PmsBrand>> recommendList(@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPageInfo<PmsBrand> brandList = homeBrandService.recommendList(pageNum, pageSize);
        return CommonResult.success(brandList.getRecords());
    }

    @Operation(summary = "获取品牌详情")
    @GetMapping(value = "/detail/{brandId}")
    public CommonResult<PmsBrand> detail(@PathVariable Long brandId) {
        PmsBrand brand = homeBrandService.detail(brandId);
        return CommonResult.success(brand);
    }

    @Operation(summary = "分页获取品牌相关商品")
    @GetMapping(value = "/productList")
    public CommonResult<CommonPage<PmsProduct>> productList(@RequestParam Long brandId,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        CommonPage<PmsProduct> result = homeBrandService.productList(brandId,pageNum, pageSize);
        return CommonResult.success(result);
    }
}
