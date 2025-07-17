package com.oyproj.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dto.OmsOrderReturnApplyResult;
import com.oyproj.admin.dto.OmsReturnApplyQueryParam;
import com.oyproj.admin.model.OmsOrderReturnApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author oy
 * @description 订单退货申请自定义Dao
 */
public interface OmsOrderReturnApplyDao {
    /**
     * 查询申请列表
     */
    Page<OmsOrderReturnApply> getList(Page<OmsOrderReturnApply> page ,@Param("queryParam") OmsReturnApplyQueryParam queryParam);

    /**
     * 获取申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id")Long id);
}
