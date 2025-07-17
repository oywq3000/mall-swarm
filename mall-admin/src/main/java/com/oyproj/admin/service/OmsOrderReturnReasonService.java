package com.oyproj.admin.service;

import com.oyproj.admin.model.OmsOrderReturnReason;
import com.oyproj.common.api.IPageInfo;

import java.util.List;

/**
 * @author oy
 * @description 订单退货原因管理Service
 */
public interface OmsOrderReturnReasonService {
    /**
     * 添加订单原因
     */
    int create(OmsOrderReturnReason returnReason);

    /**
     * 修改退货原因
     */
    int update(Long id, OmsOrderReturnReason returnReason);

    /**
     * 批量删除退货原因
     */
    int delete(List<Long> ids);

    /**
     * 分页获取退货原因
     */
    IPageInfo<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum);

    /**
     * 批量修改退货原因状态
     */
    int updateStatus(List<Long> ids, Integer status);

    /**
     * 获取单个退货原因详情信息
     */
    OmsOrderReturnReason getItem(Long id);
}
