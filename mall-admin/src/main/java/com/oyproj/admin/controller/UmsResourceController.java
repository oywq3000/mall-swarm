package com.oyproj.admin.controller;


import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.admin.service.UmsResourceService;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "UmsResourceController",description = "后台资源管理")
@RequestMapping("/resource")
@RequiredArgsConstructor
public class UmsResourceController {
    private final UmsResourceService resourceService;

    @Operation(summary = "添加后台资源")
    @PostMapping("/create")
    public CommonResult create(@RequestBody UmsResource umsResource){
        int count = resourceService.create(umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "修改后台资源")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id,
                               @RequestBody UmsResource umsResource) {
        int count = resourceService.update(id, umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "根据ID获取资源详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = resourceService.getItem(id);
        return CommonResult.success(umsResource);
    }

    @Operation(summary = "根据ID删除后台资源")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = resourceService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "分页模糊查询后台资源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsResource> pageInfo = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(pageInfo));
    }

    @Operation(summary = "查询所有后台资源")
    @GetMapping(value = "/listAll")
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> resourceList = resourceService.listAll();
        return CommonResult.success(resourceList);
    }

    @Operation(summary = "初始化路径和资源的关联数据")
    @GetMapping(value = "/initPathResourceMap")
    public CommonResult initPathResourceMap() {
        Map<String, String> pathResourceMap = resourceService.initPathResourceMap();
        return CommonResult.success(pathResourceMap);
    }
}
