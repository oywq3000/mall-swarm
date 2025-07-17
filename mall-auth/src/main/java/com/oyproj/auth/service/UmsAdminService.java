package com.oyproj.auth.service;

import com.oyproj.auth.domain.UmsAdminLoginParam;
import com.oyproj.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author oy
 * @description 后台用户服务远程调用Service
 */
@FeignClient("mall-admin")
public interface UmsAdminService {
    @PostMapping("/admin/login")
    CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam);
}
