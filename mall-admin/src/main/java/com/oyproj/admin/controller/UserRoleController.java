package com.oyproj.admin.controller;
import com.oyproj.admin.model.UmsMenu;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.admin.service.UmsRoleService;
import com.oyproj.common.api.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.oyproj.admin.model.UmsRole;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 后台用户角色管理器
 */
@RestController
@Tag(name = "UserRoleController", description = "后台用户角色管理")
@RequestMapping("/role")
@RequiredArgsConstructor
public class UserRoleController {
    private final UmsRoleService roleService;

    @Operation(summary = "添加角色")
    @PostMapping("/create")
    public CommonResult create(@RequestBody UmsRole role) {
        int count = roleService.create(role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改角色")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        int count = roleService.update(id, role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量删除角色")
    @PostMapping("delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取所有角色")
    @GetMapping( "/listAll")
    public CommonResult<List<UmsRole>> listAll() {
        List<UmsRole> roleList = roleService.list();
        return CommonResult.success(roleList);
    }
    @Operation(summary = "根据角色名称分页获取角色列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsRole>> list(@RequestParam(value = "keyword",required = false) String keyword,
                                                  @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        PageInfo<UmsRole> pageInfos = roleService.listRole(keyword,pageSize,pageNum);
        return CommonResult.success(CommonPage.restPage(pageInfos));
    }

    @Operation(summary = "修改角色状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        int count = roleService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取角色相关菜单")
    @GetMapping("/listMenu/{roleId}")
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable Long roleId) {
        List<UmsMenu> roleList = roleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @Operation(summary = "获取角色相关资源")
    @GetMapping("/listResource/{roleId}")
    public CommonResult<List<UmsResource>> listResource(@PathVariable Long roleId){
        List<UmsResource> sourceList = roleService.listResource(roleId);
        return CommonResult.success(sourceList);
    }

    @Operation(summary = "给角色分配菜单")
    @PostMapping("/allocMenu")
    public CommonResult allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        int count = roleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @Operation(summary = "给角色分配菜单")
    @PostMapping("/allocResource")
    public CommonResult allocResource(@RequestParam Long roleId,@RequestParam List<Long> resourceIds){
        int count = roleService.allocResource(roleId,resourceIds);
        return CommonResult.success(count);
    }

}
