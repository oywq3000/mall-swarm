package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.admin.mapper.UmsResourceMapper;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.admin.service.UmsResourceService;
import com.oyproj.common.constant.AuthConstant;
import com.oyproj.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceMapper,UmsResource> implements UmsResourceService {


    @Value("${spring.application.name}")
    private String applicationName;
    private final RedisService redisService;

    /**
     * 添加资源
     *
     * @param umsResource
     */
    @Override
    public int create(UmsResource umsResource) {
        return 0;
    }

    /**
     * 修改资源
     *
     * @param id
     * @param umsResource
     */
    @Override
    public int update(Long id, UmsResource umsResource) {
        return 0;
    }

    /**
     * 获取资源详情
     *
     * @param id
     */
    @Override
    public UmsResource getItem(Long id) {
        return null;
    }

    /**
     * 删除资源
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return 0;
    }

    /**
     * 分页查询资源
     *
     * @param categoryId
     * @param nameKeyword
     * @param urlKeyword
     * @param pageSize
     * @param pageNum
     */
    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 查询全部资源
     */
    @Override
    public List<UmsResource> listAll() {
        return null;
    }

    /**
     * 初始化路径与资源访问规则
     */
    @Override
    public Map<String, String> initPathResourceMap() {
        Map<String,String> pathResourceMap = new TreeMap<>();
        List<UmsResource> resourceList = listAll();
        for (UmsResource umsResource : resourceList) {
            pathResourceMap.put("/"+applicationName+umsResource.getUrl()
                    ,umsResource.getId()+":"+ umsResource.getName());
        }
        redisService.del(AuthConstant.PATH_RESOURCE_MAP);
        redisService.hSetAll(AuthConstant.PATH_RESOURCE_MAP,pathResourceMap);
        return pathResourceMap;
    }
}
