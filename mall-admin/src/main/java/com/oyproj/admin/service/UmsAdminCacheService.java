package com.oyproj.admin.service;

import com.oyproj.admin.model.UmsAdmin;

/**
 * @author oy
 * @description 后台用户缓存操作类
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 获取缓存后台用户信息
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdmin admin);
}
