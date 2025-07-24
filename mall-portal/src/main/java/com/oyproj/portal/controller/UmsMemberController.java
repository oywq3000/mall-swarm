package com.oyproj.portal.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.oyproj.common.api.CommonResult;
import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.properties.SaTokenProperties;
import com.oyproj.portal.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oy
 * @description 会员登录注册管理Controller
 */
@RestController
@Tag(name = "UmsMemberController",description = "会员登录注册管理")
@RequestMapping("/sso")
@RequiredArgsConstructor
public class UmsMemberController {

    private final UmsMemberService memberService;
    private final SaTokenProperties saTokenProperties;

    @Operation(summary = "会员注册")
    @PostMapping(value = "/register")
    public CommonResult register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String telephone,
                                 @RequestParam String authCode) {
        memberService.register(username, password, telephone, authCode);
        return CommonResult.success(null,"注册成功");
    }

    @Operation(summary = "会员登录")
    @PostMapping(value = "/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        SaTokenInfo saTokenInfo  = memberService.login(username, password);
        if (saTokenInfo  == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", saTokenInfo.getTokenValue() );
        tokenMap.put("tokenHead", saTokenProperties.getTokenPrefix()+" ");
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "获取会员信息")
    @GetMapping(value = "/info")
    public CommonResult info() {
        UmsMember member = memberService.getCurrentMember();
        return CommonResult.success(member);
    }

    @Operation(summary = "登出功能")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        memberService.logout();
        return CommonResult.success(null);
    }

    @Operation(summary = "获取验证码")
    @GetMapping(value = "/getAuthCode")
    public CommonResult getAuthCode(@RequestParam String telephone) {
        String authCode = memberService.generateAuthCode(telephone);
        return CommonResult.success(authCode,"获取验证码成功");
    }

    @Operation(summary = "修改密码")
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(@RequestParam String telephone,
                                       @RequestParam String password,
                                       @RequestParam String authCode) {
        memberService.updatePassword(telephone,password,authCode);
        return CommonResult.success(null,"密码修改成功");
    }
}
