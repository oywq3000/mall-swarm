package com.oyproj.portal.service.impl;

import com.oyproj.common.service.RedisService;
import com.oyproj.portal.model.UmsMember;
import com.oyproj.portal.properties.RedisProperties;
import com.oyproj.portal.service.UmsMemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {

    private final RedisService redisService;
    private final RedisProperties redisProperties;

    /**
     * 删除会员用户缓存
     *
     * @param memberId
     */
    @Override
    public void delMember(Long memberId) {
        String key = redisProperties.getDatabase()+":"+redisProperties.getKeys().getMember();
        redisService.del(key);
    }

    /**
     * 获取会员用户缓存
     *
     * @param memberId
     */
    @Override
    public UmsMember getMember(Long memberId) {
        String key = redisProperties.getDatabase()+":"+redisProperties.getKeys().getMember();
        return (UmsMember) redisService.get(key);
    }

    /**
     * 设置会员用户缓存
     *
     * @param member
     */
    @Override
    public void setMember(UmsMember member) {
        String key = redisProperties.getDatabase()+":"+redisProperties.getKeys().getMember();
        redisService.set(key, member,redisProperties.getExpire().getCommon());
    }

    /**
     * 设置验证码
     *
     * @param telephone
     * @param authCode
     */
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = redisProperties.getDatabase() + ":" + redisProperties.getKeys().getAuthCode() + ":" + telephone;
        redisService.set(key,authCode,redisProperties.getExpire().getAuthCode());
    }

    /**
     * 获取验证码
     *
     * @param telephone
     */
    @Override
    public String getAuthCode(String telephone) {
        String key = redisProperties.getDatabase() + ":" + redisProperties.getKeys().getAuthCode() + ":" + telephone;
        return (String) redisService.get(key);
    }
}
