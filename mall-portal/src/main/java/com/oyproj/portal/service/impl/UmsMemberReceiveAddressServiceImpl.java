package com.oyproj.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.mall.mapper.UmsMemberReceiveAddressMapper;
import com.oyproj.mall.model.UmsMember;
import com.oyproj.mall.model.UmsMemberReceiveAddress;
import com.oyproj.portal.service.UmsMemberReceiveAddressService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    private final UmsMemberService memberService;
    private final UmsMemberReceiveAddressMapper addressMapper;

    /**
     * 添加收货地址
     *
     * @param address
     */
    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address);
    }

    /**
     * 删除收货地址
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getId,id);
        return addressMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 修改收货地址
     *
     * @param id      地址表的id
     * @param address 修改的收货地址信息
     */
    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {

        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getId,id);
        //是否为默认,1表示默认
        if(address.getDefaultStatus()==1){
            //先将原来的默认地址去除
            UmsMemberReceiveAddress record= new UmsMemberReceiveAddress();
            record.setDefaultStatus(0);
            LambdaQueryWrapper<UmsMemberReceiveAddress> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
            updateWrapper.eq(UmsMemberReceiveAddress::getDefaultStatus,1);
            addressMapper.update(record,updateWrapper);
        }
        return addressMapper.update(address,lambdaQueryWrapper);
    }

    /**
     * 返回当前用户的收货地址
     */
    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        return addressMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 获取地址详情
     *
     * @param id 地址id
     */
    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        return addressMapper.selectOne(lambdaQueryWrapper);
    }
}
