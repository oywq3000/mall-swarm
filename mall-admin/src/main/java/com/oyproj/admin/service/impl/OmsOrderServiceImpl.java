package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.OmsOrderDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.*;
import com.oyproj.mall.mapper.OmsOrderMapper;
import com.oyproj.mall.mapper.OmsOrderOperateHistoryMapper;
import com.oyproj.mall.model.OmsOrder;
import com.oyproj.mall.model.OmsOrderOperateHistory;
import com.oyproj.admin.service.OmsOrderService;
import com.oyproj.common.api.IPageInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OmsOrderServiceImpl implements OmsOrderService {


    private final OmsOrderMapper orderMapper;

    private final OmsOrderDao orderDao;

    private final OmsOrderOperateHistoryMapper orderOperateHistoryMapper;

    /**
     * 订单查询
     *
     * @param queryParam
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        Page<OmsOrder> page1 = new Page<>(pageNum,pageSize);
        Page<OmsOrder> list = orderDao.getList(page1, queryParam);
        return PageInfo.build(list);
    }

    /**
     * 批量发货
     *
     * @param deliveryParamList
     */
    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = orderDao.delivery(deliveryParamList);
        //添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).toList();

        orderOperateHistoryMapper.insert(operateHistoryList);
        return count;
    }

    /**
     * 批量关闭订单
     *
     * @param ids
     * @param note
     */
    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder record = new OmsOrder();
        record.setStatus(4);
        LambdaQueryWrapper<OmsOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsOrder::getDeleteStatus,0);
        lambdaQueryWrapper.in(OmsOrder::getId,ids);

        int count = orderMapper.update(record, lambdaQueryWrapper);
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(4);
            history.setNote("订单关闭:"+note);
            return history;
        }).collect(Collectors.toList());
        orderOperateHistoryMapper.insert(historyList);
        return count;
    }

    /**
     * 批量删除订单
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        LambdaQueryWrapper<OmsOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsOrder::getDeleteStatus,0);
        lambdaQueryWrapper.in(OmsOrder::getId,ids);
        return orderMapper.update(record, lambdaQueryWrapper);
    }

    /**
     * 获取指定订单详情
     *
     * @param id
     */
    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDao.getDetail(id);
    }

    /**
     * 修改订单收货人信息
     *
     * @param receiverInfoParam
     */
    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = orderMapper.updateById(order);
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    /**
     * 修改订单费用信息
     *
     * @param moneyInfoParam
     */
    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        int count = orderMapper.updateById(order);

        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    /**
     * 修改订单备注
     *
     * @param id
     * @param note
     * @param status
     */
    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = orderMapper.updateById(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(status);
        history.setNote("修改备注信息："+note);
        orderOperateHistoryMapper.insert(history);
        return count;
    }
}
