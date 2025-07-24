package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.oyproj.mall.mapper.CmsPrefrenceAreaMapper;

import com.oyproj.mall.model.CmsPrefrenceArea;
import com.oyproj.admin.service.CmsPreferenceAreaService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CmsPreferenceAreaServiceImpl implements CmsPreferenceAreaService {

    private final CmsPrefrenceAreaMapper preferenceAreaMapper;
    /**
     * 获取所有优选专区
     */
    @Override
    public List<CmsPrefrenceArea> listAll() {
        return preferenceAreaMapper.selectList(new LambdaQueryWrapper<>());
    }
}
