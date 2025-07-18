package com.oyproj.admin.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * @author oy
 * @description 修改订单费用信息参数
 */

@Getter
@Setter
public class OmsMoneyInfoParam {
    @Schema(title = "订单ID")
    private Long orderId;
    @Schema(title = "运费金额")
    private BigDecimal freightAmount;
    @Schema(title = "管理员后台调整订单使用的折扣金额")
    private BigDecimal discountAmount;
    @Schema(title = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
}
