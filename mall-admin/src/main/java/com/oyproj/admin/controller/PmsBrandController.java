package com.oyproj.admin.controller;

import com.oyproj.admin.dto.PmsBrandParam;
import com.oyproj.mall.model.PmsBrand;
import com.oyproj.admin.service.PmsBrandService;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 商品品牌管理Controller
 */
@RestController
@Tag(name = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class PmsBrandController {

    private final PmsBrandService brandService;

    @Operation(summary = "获取全部品牌列表")
    @GetMapping(value = "/listAll")
    public CommonResult<List<PmsBrand>> getList() {
        return CommonResult.success(brandService.listAllBrand());
    }

    @Operation(summary = "添加品牌")
    @PostMapping(value = "/create")
    public CommonResult create(@Validated @RequestBody PmsBrandParam pmsBrand) {
        CommonResult commonResult;
        int count = brandService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @Operation(summary = "更新品牌")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable("id") Long id,
                               @Validated @RequestBody PmsBrandParam pmsBrandParam) {
        CommonResult commonResult;
        int count = brandService.updateBrand(id, pmsBrandParam);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @Operation(summary = "删除品牌")
    @GetMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable("id") Long id) {
        int count = brandService.deleteBrand(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "根据品牌名称分页获取品牌列表")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        IPageInfo<PmsBrand> brandList = brandService.listBrand(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @Operation(summary = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsBrand> getItem(@PathVariable("id") Long id) {
        return CommonResult.success(brandService.getBrand(id));
    }

    @Operation(summary = "批量删除品牌")
    @PostMapping(value = "/delete/batch")
    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "批量更新显示状态")
    @PostMapping(value = "/update/showStatus")
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids,
                                         @RequestParam("showStatus") Integer showStatus) {
        int count = brandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "批量更新厂家制造商状态")
    @PostMapping(value = "/update/factoryStatus")
    public CommonResult updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = brandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
