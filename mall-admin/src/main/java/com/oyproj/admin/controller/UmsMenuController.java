package com.oyproj.admin.controller;

import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.UmsMenuNode;
import com.oyproj.mall.model.UmsMenu;
import com.oyproj.admin.service.UmsMenuService;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 后台菜单管理Controller
 */
@RestController
@Tag(name = "UmsMenuController", description = "后台菜单管理")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class UmsMenuController {
    private final UmsMenuService menuService;

    @Operation(summary = "添加后台菜单")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody UmsMenu umsMenu) {
        int count = menuService.create(umsMenu);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "修改后台菜单")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id,
                               @RequestBody UmsMenu umsMenu) {
        int count = menuService.update(id, umsMenu);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "根据ID获取菜单详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsMenu> getItem(@PathVariable Long id) {
        UmsMenu umsMenu = menuService.getItem(id);
        return CommonResult.success(umsMenu);
    }

    @Operation(summary = "根据ID删除后台菜单")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = menuService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "分页查询后台菜单")
    @GetMapping(value = "/list/{parentId}")
    public CommonResult<CommonPage<UmsMenu>> list(@PathVariable Long parentId,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsMenu> pageInfo = menuService.list(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(pageInfo));
    }

    @Operation(summary = "树形结构返回所有菜单列表")
    @GetMapping(value = "/treeList")
    public CommonResult<List<UmsMenuNode>> treeList() {
        List<UmsMenuNode> list = menuService.treeList();
        return CommonResult.success(list);
    }

    @Operation(summary = "修改菜单显示状态")
    @PostMapping(value = "/updateHidden/{id}")
    public CommonResult updateHidden(@PathVariable Long id, @RequestParam("hidden") Integer hidden) {
        int count = menuService.updateHidden(id, hidden);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

}
