package com.oyproj.admin.dto;

import com.oyproj.mall.model.OmsCompanyAddress;
import com.oyproj.mall.model.OmsOrderReturnApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author oy
 * @description 申请信息封装
 */
@Getter
@Setter
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {
    @Schema(title = "公司收货地址")
    private OmsCompanyAddress companyAddress;
}
