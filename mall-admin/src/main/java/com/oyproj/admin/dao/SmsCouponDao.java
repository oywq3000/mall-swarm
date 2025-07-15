package com.oyproj.admin.dao;

import com.oyproj.admin.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

/**
 * @author oy
 * @description 自定义优惠卷管理Dao
 */
public interface SmsCouponDao {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(@Param("id") Long id);
}
