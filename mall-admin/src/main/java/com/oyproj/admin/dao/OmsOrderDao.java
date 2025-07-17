package com.oyproj.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dto.OmsOrderDeliveryParam;
import com.oyproj.admin.dto.OmsOrderDetail;
import com.oyproj.admin.dto.OmsOrderQueryParam;
import com.oyproj.admin.model.OmsOrder;
import com.oyproj.common.api.IPageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author oy
 * @description 订单自定义查序Dao
 */
public interface OmsOrderDao {
    /**
     * 条件查询订单
     */
    Page<OmsOrder> getList(Page<OmsOrder> page, @Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
