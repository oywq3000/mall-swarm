package com.oyproj.admin.controller;

import com.oyproj.mall.model.PmsSkuStock;
import com.oyproj.admin.service.PmsSkuStockService;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description sku库存Controller
 */
@RestController
@Tag(name = "PmsSkuStockController", description = "sku商品库存管理")
@RequestMapping("/sku")
@RequiredArgsConstructor
public class PmsSkuStockController {
    private final PmsSkuStockService skuStockService;
    @Operation(summary = "根据商品编号及编号模糊搜索sku库存")
    @GetMapping(value = "/{pid}")
    public CommonResult<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<PmsSkuStock> skuStockList = skuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
    @Operation(summary = "批量更新库存信息")
    @GetMapping(value ="/update/{pid}")
    public CommonResult update(@PathVariable Long pid,@RequestBody List<PmsSkuStock> skuStockList){
        int count = skuStockService.update(pid,skuStockList);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
