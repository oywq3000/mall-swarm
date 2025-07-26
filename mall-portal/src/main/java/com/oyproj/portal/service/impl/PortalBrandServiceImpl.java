package com.oyproj.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.mapper.PmsBrandMapper;
import com.oyproj.mall.mapper.PmsProductMapper;
import com.oyproj.mall.model.PmsBrand;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.portal.dao.HomeDao;
import com.oyproj.portal.domain.PageInfo;
import com.oyproj.portal.service.PortalBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortalBrandServiceImpl implements PortalBrandService {


    private final HomeDao homeDao;

    private final PmsBrandMapper brandMapper;

    private final PmsProductMapper productMapper;
    /**
     * 分页获取推荐品牌
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public IPageInfo<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        Page<PmsBrand> page1 = new Page<>(pageNum,pageSize);
        Page<PmsBrand> recommendBrandList = homeDao.getRecommendBrandList(page1);

        return PageInfo.build(recommendBrandList) ;
    }

    /**
     * 获取品牌详情
     *
     * @param brandId
     */
    @Override
    public PmsBrand detail(Long brandId) {
        return brandMapper.selectById(brandId);
    }

    /**
     * 分页获取品牌关联商品
     *
     * @param brandId
     * @param pageNum
     * @param pageSize
     */
    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        Page<PmsProduct> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getDeleteStatus,0).eq(PmsProduct::getBrandId,brandId);
        Page<PmsProduct> pmsProductPage = productMapper.selectPage(page1, lambdaQueryWrapper);
        return CommonPage.restPage(PageInfo.build(pmsProductPage));
    }
}
