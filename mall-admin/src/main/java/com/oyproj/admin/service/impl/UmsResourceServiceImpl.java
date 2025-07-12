package com.oyproj.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.mapper.UmsResourceMapper;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.admin.service.UmsResourceService;
import com.oyproj.common.constant.AuthConstant;
import com.oyproj.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        umsResource.setCreateTime(new Date());
        boolean isSave = save(umsResource);
        if(isSave){
            initPathResourceMap();
            return 1;
        }
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
        umsResource.setId(id);
        boolean isUpdated = updateById(umsResource);
        if(isUpdated){
            initPathResourceMap();
            return 1;
        }
        return 0;

    }

    /**
     * 获取资源详情
     *
     * @param id
     */
    @Override
    public UmsResource getItem(Long id) {
          return getById(id);
    }

    /**
     * 删除资源
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        boolean isRemoved = removeById(id);
        if(isRemoved){
            initPathResourceMap();
            return 1;
        }
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
    public PageInfo<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        LambdaQueryWrapper<UmsResource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(categoryId!=null){
            lambdaQueryWrapper.eq(UmsResource::getCategoryId,categoryId);
        }
        if(StrUtil.isNotBlank(nameKeyword)){
            lambdaQueryWrapper.like(UmsResource::getName,nameKeyword);
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            lambdaQueryWrapper.like(UmsResource::getUrl,urlKeyword);
        }
        Page<UmsResource> page1 = new Page<>(pageNum,pageSize);
        Page<UmsResource> umsResourcePage = baseMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(umsResourcePage);
    }

    /**
     * 查询全部资源
     */
    @Override
    public List<UmsResource> listAll() {
        return list();
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
