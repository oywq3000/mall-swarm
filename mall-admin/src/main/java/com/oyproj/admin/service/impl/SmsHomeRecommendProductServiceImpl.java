package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsHomeRecommendProductMapper;
import com.oyproj.mall.model.SmsHomeRecommendProduct;
import com.oyproj.admin.service.SmsHomeRecommendProductService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {

    private final SmsHomeRecommendProductMapper recommendProductMapper;
    /**
     * 添加首页推荐
     *
     * @param homeRecommendProductList
     */
    @Override
    public int create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
        }
        recommendProductMapper.insert(homeRecommendProductList);
        return homeRecommendProductList.size();
    }

    /**
     * 修改推荐排序
     *
     * @param id
     * @param sort
     */
    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return recommendProductMapper.updateById(recommendProduct);
    }

    /**
     * 批量删除推荐
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        return recommendProductMapper.deleteByIds(ids);
    }

    /**
     * 更新推荐状态
     *
     * @param ids
     * @param recommendStatus
     */
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaQueryWrapper<SmsHomeRecommendProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SmsHomeRecommendProduct::getId,ids);
        SmsHomeRecommendProduct record = new SmsHomeRecommendProduct();
        record.setRecommendStatus(recommendStatus);
        return recommendProductMapper.update(record,lambdaQueryWrapper);
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
    public IPageInfo<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeRecommendProduct> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SmsHomeRecommendProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(!StringUtils.isEmpty(productName)){
            lambdaQueryWrapper.like(SmsHomeRecommendProduct::getProductName,productName);
        }
        if(recommendStatus!=null){
            lambdaQueryWrapper.eq(SmsHomeRecommendProduct::getRecommendStatus,recommendStatus);
        }
        Page<SmsHomeRecommendProduct> page = recommendProductMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }
}
