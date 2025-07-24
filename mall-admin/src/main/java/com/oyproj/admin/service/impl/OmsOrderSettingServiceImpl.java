package com.oyproj.admin.service.impl;

import com.oyproj.mall.mapper.OmsOrderSettingMapper;
import com.oyproj.mall.model.OmsOrderSetting;
import com.oyproj.admin.service.OmsOrderSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {
    private final OmsOrderSettingMapper orderSettingMapper;
    /**
     * 获取指定订单设置
     *
     * @param id
     */
    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectById(id);
    }

    /**
     * 修改指定订单设置
     *
     * @param id
     * @param orderSetting
     */
    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
       orderSetting.setId(id);
       return orderSettingMapper.updateById(orderSetting);
    }
}
