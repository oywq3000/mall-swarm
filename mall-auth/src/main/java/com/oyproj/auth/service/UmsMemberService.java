package com.oyproj.auth.service;


import com.oyproj.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author oy
 * @description 前台会员服务远程调用Service
 */
@FeignClient("mall-portal")
public interface UmsMemberService {
    @PostMapping("/sso/login")
    CommonResult login(@RequestParam("username") String username,@RequestParam("password") String password);
}
