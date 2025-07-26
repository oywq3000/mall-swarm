package com.oyproj.portal.service;

import com.oyproj.portal.dto.OmsOrderReturnApplyParam;

/**
 * @author oy
 * @description 订单退货管理Service
 */
public interface OmsPortalOrderReturnApplyService {
    /**
     * 提交申请
     */
    int create(OmsOrderReturnApplyParam returnApplyParam);
}
