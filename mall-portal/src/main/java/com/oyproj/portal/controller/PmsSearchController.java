package com.oyproj.portal.controller;

import com.mall.api.client.SearchClient;
import com.mall.api.dto.search.EsProduct;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.mapper.PmsSearchHistoryMapper;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.mall.model.PmsSearchHistory;
import com.oyproj.portal.service.PmsSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsSearchController",description = "用户商品搜索Controller")
@RequestMapping("/search")
@RequiredArgsConstructor
public class PmsSearchController {

    private final PmsSearchService searchService;
    private final SearchClient searchClient;

    @Operation(description = "搜索商品")
    @Parameter(name = "sort",description = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低",
    in = ParameterIn.QUERY,schema = @Schema(type = "integer",defaultValue = "0",allowableValues = {"0","1","2","3","4"}))
    @GetMapping("/search")
    CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                @RequestParam(required = false, defaultValue = "0") Integer sort){
        //IPageInfo<PmsProduct> productList = searchService.search(keyword,pageNum,pageSize,sort);
        //对应后端而言前端page从1开始，而后端page从0开始。
        pageNum--;
        CommonResult<CommonPage<EsProduct>> search = searchClient.search(keyword, pageNum, pageSize, sort);
        return search;
    }

    @Operation(description = "删除搜索历史记录")
    @PostMapping("/delete")
    CommonResult deleteSearchHistory(@RequestParam("ids") List<Long> ids){
        int count = searchService.dropSearchHistory(ids);
        return CommonResult.success(count);
    }

    @Operation(description = "获得搜索历史记录")
    @GetMapping("/history")
    CommonResult<List<PmsSearchHistory>> getHistory(){
        List<PmsSearchHistory> searchHistory = searchService.getSearchHistory();
        return CommonResult.success(searchHistory);
    }

}
