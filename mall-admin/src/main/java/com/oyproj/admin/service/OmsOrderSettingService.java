package com.oyproj.admin.service;

import com.oyproj.admin.model.OmsOrderSetting;

/**
 * @author oy
 * @description 订单设置Controller
 */
public interface OmsOrderSettingService {
    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
