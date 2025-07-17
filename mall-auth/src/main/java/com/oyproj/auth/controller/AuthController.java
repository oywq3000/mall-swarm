package com.oyproj.auth.controller;


import com.oyproj.auth.domain.UmsAdminLoginParam;
import com.oyproj.auth.service.UmsAdminService;
import com.oyproj.auth.service.UmsMemberService;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.constant.AuthConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oy
 * @description 统一认证授权接口
 */
@RestController
@Tag(name = "AuthController",description = "统一认证授权接口")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UmsAdminService adminService;
    private final UmsMemberService memberService;

    @Operation(summary = "登入以后返回token")
    @PostMapping(value = "/login")
    public CommonResult login(@RequestParam String clientId,
                              @RequestParam String username,
                              @RequestParam String password){
        if(AuthConstant.ADMIN_CLIENT_ID.equals(clientId)){
            UmsAdminLoginParam loginParam = new UmsAdminLoginParam();
            loginParam.setUsername(username);
            loginParam.setPassword(password);
            return adminService.login(loginParam);
        }else if(AuthConstant.PORTAL_CLIENT_ID.equals(clientId)){
            return memberService.login(username,password);
        }else{
            return CommonResult.forbidden("clientId不正确");
        }
    }
}
