package com.mall.api.client;

import com.mall.api.dto.search.EsProduct;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-search")
public interface SearchClient {
    @Operation(summary = "综合搜索,排序")
    @GetMapping(value = "/esProduct/search")
    CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                               @RequestParam(required = false, defaultValue = "0") Integer sort);
}
