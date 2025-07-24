package com.oyproj.admin.dto;

import com.oyproj.mall.model.OmsOrderItem;
import com.oyproj.mall.model.OmsOrderOperateHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author oy
 * @description 订单详情信息
 */
@Getter
@Setter
public class OmsOrderDetail {
    @Schema(title = "订单商品列表")
    private List<OmsOrderItem> orderItemList;
    @Schema(title = "订单操作记录列表")
    private List<OmsOrderOperateHistory> historyList;
}
