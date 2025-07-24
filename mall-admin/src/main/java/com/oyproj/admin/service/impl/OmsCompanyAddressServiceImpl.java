package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.mall.mapper.OmsCompanyAddressMapper;
import com.oyproj.mall.model.OmsCompanyAddress;
import com.oyproj.admin.service.OmsCompanyAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {

    private final OmsCompanyAddressMapper companyAddressMapper;
    /**
     * 获取全部收货地址
     */
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectList(new LambdaQueryWrapper<>());
    }
}
