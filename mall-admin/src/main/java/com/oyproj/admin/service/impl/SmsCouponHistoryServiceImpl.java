package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.mapper.SmsCouponHistoryMapper;
import com.oyproj.admin.model.SmsCouponHistory;
import com.oyproj.admin.service.SmsCouponHistoryService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {

    private final SmsCouponHistoryMapper couponHistoryMapper;
    /**
     * 分页查序优惠卷领取记录
     *
     * @param couponId  优惠券id
     * @param useStatus 使用状态
     * @param orderSn   使用订单号码
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public IPageInfo<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        LambdaQueryWrapper<SmsCouponHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<SmsCouponHistory> page1 = new Page<>(pageNum,pageSize);
        if(couponId!=null){
            lambdaQueryWrapper.eq(SmsCouponHistory::getCouponId,couponId);
        }
        if(useStatus!=null){
            lambdaQueryWrapper.eq(SmsCouponHistory::getUseStatus,useStatus);
        }
        if(!StringUtils.isEmpty(orderSn)){
            lambdaQueryWrapper.eq(SmsCouponHistory::getOrderSn,orderSn);
        }
        Page<SmsCouponHistory> smsCouponHistoryPage = couponHistoryMapper.selectPage(page1, lambdaQueryWrapper);

        return PageInfo.build(smsCouponHistoryPage);
    }
}
