package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsHomeNewProductMapper;
import com.oyproj.mall.model.SmsHomeNewProduct;
import com.oyproj.admin.service.SmsHomeNewProductService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {
    private final SmsHomeNewProductMapper homeNewProductMapper;

    /**
     * 添加首页推荐
     *
     * @param homeNewProductList
     */
    @Override
    public int create(List<SmsHomeNewProduct> homeNewProductList) {
        for (SmsHomeNewProduct SmsHomeNewProduct : homeNewProductList) {
            SmsHomeNewProduct.setRecommendStatus(1);
            SmsHomeNewProduct.setSort(0);
        }
        homeNewProductMapper.insert(homeNewProductList);
        return homeNewProductList.size();
    }

    /**
     * 修改推荐排序
     *
     * @param id
     * @param sort
     */
    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return homeNewProductMapper.updateById(homeNewProduct);
    }

    /**
     * 批量删除推荐
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        return homeNewProductMapper.deleteByIds(ids);
    }

    /**
     * 更新推荐状态
     *
     * @param ids
     * @param recommendStatus
     */
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaQueryWrapper<SmsHomeNewProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SmsHomeNewProduct::getProductId,ids);
        SmsHomeNewProduct record = new SmsHomeNewProduct();
        record.setRecommendStatus(recommendStatus);
        return homeNewProductMapper.update(record,lambdaQueryWrapper);
    }

    /**
     * 分页查询推荐
     *
     * @param productName
     * @param recommendStatus
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeNewProduct> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SmsHomeNewProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(!StringUtils.isEmpty(productName)){
            lambdaQueryWrapper.like(SmsHomeNewProduct::getProductName,productName);
        }
        if(recommendStatus!=null){
            lambdaQueryWrapper.eq(SmsHomeNewProduct::getRecommendStatus,recommendStatus);
        }
        Page<SmsHomeNewProduct> page = homeNewProductMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }
}
