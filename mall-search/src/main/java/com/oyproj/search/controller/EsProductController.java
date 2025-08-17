package com.oyproj.search.controller;

import com.mall.api.dto.search.EsProduct;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.mall.api.dto.search.EsProductRelatedInfo;
import com.oyproj.search.service.EsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 搜索商品管理Controller
 */

@RestController
@Tag(name = "EsProductController", description = "搜索商品管理")
@RequestMapping("/esProduct")
@RequiredArgsConstructor
public class EsProductController {
    @Autowired
    private EsProductService esProductService;

    @Operation(summary = "导入所有数据库中商品到ES")
    @GetMapping(value = "/importAll")
    public CommonResult<Integer> importAllList() {
        int count = esProductService.importAll();
        return CommonResult.success(count);
    }

    @Operation(summary = "根据id删除商品")
    @GetMapping(value = "/delete/{id}")
    public CommonResult<Object> delete(@PathVariable Long id) {
        esProductService.delete(id);
        return CommonResult.success(null);
    }

    @Operation(summary = "根据id批量删除商品")
    @PostMapping(value = "/delete/batch")
    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
        esProductService.delete(ids);
        return CommonResult.success(null);
    }

    @Operation(summary = "根据id创建商品")
    @PostMapping(value = "/create/{id}")
    public CommonResult<EsProduct> create(@PathVariable Long id) {
        EsProduct esProduct = esProductService.create(id);
        if (esProduct != null) {
            return CommonResult.success(esProduct);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "简单搜索")
    @GetMapping(value = "/search/simple")
    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }

    @Operation(summary = "综合搜索,排序")
    @GetMapping(value = "/search")
    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                      @RequestParam(required = false, defaultValue = "0") Integer sort) {
        //后端pageNum从0开始数
        pageNum--;
        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize,sort);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }

    @Operation(summary = "综合搜索、筛选、排序")
    @Parameter(name = "sort", description = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低", in = ParameterIn.QUERY, schema = @Schema(type = "integer",defaultValue = "0",allowableValues = {"0","1","2","3","4"}))
    @GetMapping(value = "/search/local")
    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Long brandId,
                                                      @RequestParam(required = false) Long productCategoryId,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                      @RequestParam(required = false, defaultValue = "0") Integer sort) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }

    @Operation(summary = "根据商品id推荐商品")
    @GetMapping(value = "/recommend/{id}")
    public CommonResult<CommonPage<EsProduct>> recommend(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProductPage = esProductService.recommend(id, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }

    @Operation(summary = "获取搜索的相关品牌、分类及筛选属性")
    @GetMapping(value = "/search/relate")
    public CommonResult<EsProductRelatedInfo> searchRelatedInfo(@RequestParam(required = false) String keyword) {
        EsProductRelatedInfo productRelatedInfo = esProductService.searchRelatedInfo(keyword);
        return CommonResult.success(productRelatedInfo);
    }
}
