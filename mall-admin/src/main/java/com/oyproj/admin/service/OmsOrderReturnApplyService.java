package com.oyproj.admin.service;

import com.oyproj.admin.dto.OmsOrderReturnApplyResult;
import com.oyproj.admin.dto.OmsReturnApplyQueryParam;
import com.oyproj.admin.dto.OmsUpdateStatusParam;
import com.oyproj.mall.model.OmsOrderReturnApply;
import com.oyproj.common.api.IPageInfo;

import java.util.List;

/**
 * @author oy
 * @description 退货申请管理Service
 */
public interface OmsOrderReturnApplyService {
    /**
     * 分页查询申请
     */
    IPageInfo<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     */
    int delete(List<Long> ids);

    /**
     * 修改申请状态
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * 获取指定申请详情
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
