package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsHomeAdvertiseMapper;
import com.oyproj.mall.model.SmsHomeAdvertise;
import com.oyproj.admin.service.SmsHomeAdvertiseService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author oy
 * @description 首页广告管理Service实现类
 */
@Service
@RequiredArgsConstructor
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {

    private final SmsHomeAdvertiseMapper advertiseMapper;

    @Override
    public int create(SmsHomeAdvertise advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        return advertiseMapper.insert(advertise);
    }

    @Override
    public int delete(List<Long> ids) {
        return advertiseMapper.deleteByIds(ids);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsHomeAdvertise record = new SmsHomeAdvertise();
        record.setId(id);
        record.setStatus(status);
        return advertiseMapper.updateById(record);
    }

    @Override
    public SmsHomeAdvertise getItem(Long id) {
        return advertiseMapper.selectById(id);
    }

    @Override
    public int update(Long id, SmsHomeAdvertise advertise) {
        advertise.setId(id);
        return advertiseMapper.updateById(advertise);
    }

    @Override
    public IPageInfo<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        Page<SmsHomeAdvertise> page1 = new Page<>(pageNum,pageSize);

        LambdaQueryWrapper<SmsHomeAdvertise> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(name)) {
            lambdaQueryWrapper.like(SmsHomeAdvertise::getName,name);
        }
        if (type != null) {
            lambdaQueryWrapper.eq(SmsHomeAdvertise::getType,type);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String startStr = endTime + " 00:00:00";
            String endStr = endTime + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            try {
                start = sdf.parse(startStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date end = null;
            try {
                end = sdf.parse(endStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (start != null && end != null) {
                lambdaQueryWrapper.between(SmsHomeAdvertise::getEndTime,start,endTime);
            }
        }
        lambdaQueryWrapper.orderByDesc(SmsHomeAdvertise::getSort);
        Page<SmsHomeAdvertise> smsHomeAdvertiseIPage = advertiseMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(smsHomeAdvertiseIPage);
    }
}
