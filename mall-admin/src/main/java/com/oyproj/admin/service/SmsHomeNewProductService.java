package com.oyproj.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oyproj.admin.model.SmsHomeNewProduct;
import com.oyproj.common.api.IPageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author oy
 * @description 首页新品管理Service
 */
public interface SmsHomeNewProductService {
    /**
     * 添加首页推荐
     */
    @Transactional
    int create(List<SmsHomeNewProduct> homeNewProductList);

    /**
     * 修改推荐排序
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐
     */
    int delete(List<Long> ids);

    /**
     * 更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐
     */
    IPageInfo<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
