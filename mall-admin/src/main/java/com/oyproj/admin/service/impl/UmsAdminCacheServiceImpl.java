package com.oyproj.admin.service.impl;

import com.oyproj.admin.model.UmsAdmin;
import com.oyproj.admin.properties.RedisProperties;
import com.oyproj.admin.service.UmsAdminCacheService;
import com.oyproj.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    private final RedisService redisService;
    private final RedisProperties redisProperties;


    /**
     * 删除后台用户缓存
     *
     * @param adminId
     */
    @Override
    public void delAdmin(Long adminId) {
        String key  =redisProperties.getDatabase()+":"
                + redisProperties.getKey().getAdmin()+":"
                + adminId;
        redisService.del(key);
    }

    /**
     * 获取缓存后台用户信息
     *
     * @param adminId
     */
    @Override
    public UmsAdmin getAdmin(Long adminId) {
        String key = redisProperties.getDatabase() + ":" + redisProperties.getKey().getAdmin() + ":" + adminId;
        return (UmsAdmin) redisService.get(key);
    }

    /**
     * 设置缓存后台用户信息
     *
     * @param admin
     */
    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = redisProperties.getDatabase() + ":" + redisProperties.getKey().getAdmin() + ":" + admin.getId();
        redisService.set(key,admin,redisProperties.getExpire().getCommon());
    }
}
