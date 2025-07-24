package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsHomeBrandMapper;
import com.oyproj.mall.model.SmsHomeBrand;
import com.oyproj.admin.service.SmsHomeBrandService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsHomeBrandServiceImpl implements SmsHomeBrandService {
    private final SmsHomeBrandMapper homeBrandMapper;
    @Override
    public int create(List<SmsHomeBrand> homeBrandList) {
        for (SmsHomeBrand smsHomeBrand : homeBrandList) {
            smsHomeBrand.setRecommendStatus(1);
            smsHomeBrand.setSort(0);
        }
        homeBrandMapper.insert(homeBrandList);
        return homeBrandList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeBrand homeBrand = new SmsHomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        return homeBrandMapper.updateById(homeBrand);
    }

    @Override
    public int delete(List<Long> ids) {
        return homeBrandMapper.deleteByIds(ids);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaQueryWrapper<SmsHomeBrand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SmsHomeBrand::getId,ids);
        SmsHomeBrand record = new SmsHomeBrand();
        record.setRecommendStatus(recommendStatus);
        return homeBrandMapper.update(record,lambdaQueryWrapper);
    }

    @Override
    public IPageInfo<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeBrand> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SmsHomeBrand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(brandName)){
            lambdaQueryWrapper.like(SmsHomeBrand::getBrandName,brandName);
        }
        if(recommendStatus!=null){
            lambdaQueryWrapper.eq(SmsHomeBrand::getRecommendStatus,recommendStatus);
        }
        Page<SmsHomeBrand> page = homeBrandMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }
}
