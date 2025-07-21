package com.oyproj.admin.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.collection.CollUtil;
import com.oyproj.admin.dto.UmsAdminLoginParam;
import com.oyproj.admin.dto.UmsAdminParam;
import com.oyproj.admin.dto.UpdateAdminPasswordParam;
import com.oyproj.admin.model.UmsAdmin;
import com.oyproj.admin.model.UmsRole;
import com.oyproj.admin.properties.SaTokenProperties;
import com.oyproj.admin.service.UmsAdminService;
import com.oyproj.admin.service.UmsRoleService;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author oy
 * @description 后台用户管理
 */
@RestController
@Tag(name = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UmsAdminController {
    private final UmsAdminService adminService;

    private final UmsRoleService roleService;

    private final SaTokenProperties saTokenProperties;

    @Operation(summary = "用户注册")
    @PostMapping(value = "/register")
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @Operation(summary = "登录以后返回token")
    @PostMapping(value = "/login")
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        SaTokenInfo saTokenInfo  = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (saTokenInfo  == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", saTokenInfo.getTokenValue() );
        tokenMap.put("tokenHead", saTokenProperties.getTokenPrefix()+" ");
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping(value = "/info")
    @ResponseBody
    public CommonResult getAdminInfo() {
        UmsAdmin umsAdmin = adminService.getCurrentAdmin();
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = adminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return CommonResult.success(data);
    }

    @Operation(summary = "登出功能")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        adminService.logout();
        return CommonResult.success(null);
    }
    @Operation(summary = "根据用户名或姓名分页获取用户列表")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPageInfo<UmsAdmin> pageInfo = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(pageInfo));
    }

    @Operation(summary = "修改指定用户信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        int count = adminService.update(id, admin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改指定用户密码")
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(@RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "删除指定用户信息")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改帐号状态")
    @PostMapping(value = "/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        int count = adminService.update(id,umsAdmin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "给用户分配角色")
    @PostMapping(value = "/role/update")
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取指定用户的角色")
    @GetMapping(value = "/role/{adminId}")
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }
}
