package com.oyproj.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author oy
 * @description 订单退货申请查序参数
 */
@Getter
@Setter
public class OmsReturnApplyQueryParam {
    @Schema(title = "服务单号")
    private Long id;
    @Schema(title = "收货人姓名/号码")
    private String receiverKeyword;
    @Schema(title = "申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;
    @Schema(title = "申请时间")
    private String createTime;
    @Schema(title = "处理人员")
    private String handleMan;
    @Schema(title = "处理时间")
    private String handleTime;
}
