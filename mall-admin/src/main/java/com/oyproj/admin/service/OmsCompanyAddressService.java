package com.oyproj.admin.service;



import com.oyproj.mall.model.OmsCompanyAddress;

import java.util.List;

/**
 * @author oy
 * @description 收货地址管Service
 */
public interface OmsCompanyAddressService {
    /**
     * 获取全部收货地址
     */
    List<OmsCompanyAddress> list();
}
