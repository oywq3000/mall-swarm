package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.SmsCouponDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.SmsCouponParam;
import com.oyproj.mall.mapper.SmsCouponMapper;
import com.oyproj.mall.mapper.SmsCouponProductCategoryRelationMapper;
import com.oyproj.mall.mapper.SmsCouponProductRelationMapper;
import com.oyproj.mall.model.SmsCoupon;
import com.oyproj.mall.model.SmsCouponProductCategoryRelation;
import com.oyproj.mall.model.SmsCouponProductRelation;
import com.oyproj.admin.service.SmsCouponService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SmsCouponServiceImpl implements SmsCouponService {
    private final SmsCouponMapper couponMapper;
    private final SmsCouponProductRelationMapper couponProductRelationMapper;
    private final SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;
    private final SmsCouponDao couponDao;
    /**
     * 添加优惠券
     *
     * @param couponParam
     */
    @Override
    public int create(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        int count = couponMapper.insert(couponParam);
        //插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for (SmsCouponProductRelation smsCouponProductRelation : couponParam.getProductRelationList()) {
                smsCouponProductRelation.setCouponId(couponParam.getId());
            }
            couponProductRelationMapper.insert(couponParam.getProductRelationList());
        }
        //优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            productCategoryRelationMapper.insert(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    /**
     * 根据优惠券id删除优惠券
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        //删除优惠券
        int count = couponMapper.deleteById(id);
        //删除商品关联
        deleteProductRelation(id);
        //删除商品分类关联
        deleteProductCategoryRelation(id);
        return count;
    }



    /**
     * 根据优惠券id更新优惠券信息
     *
     * @param id
     * @param couponParam
     */
    @Override
    public int update(Long id, SmsCouponParam couponParam) {
        couponParam.setId(id);
        int count = couponMapper.updateById(couponParam);
        //删除后插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(id);
            couponProductRelationMapper.insert(couponParam.getProductRelationList());
        }
        //删除后插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(id);
            productCategoryRelationMapper.insert(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    /**
     * 分页获取优惠券列表
     *
     * @param name
     * @param type
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        Page<SmsCoupon> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SmsCoupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            lambdaQueryWrapper.like(SmsCoupon::getName,name);
        }
        if(type!=null){
            lambdaQueryWrapper.like(SmsCoupon::getType,type);
        }
        Page<SmsCoupon> page = couponMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }

    /**
     * 获取优惠券详情
     *
     * @param id 优惠券表id
     */
    @Override
    public SmsCouponParam getItem(Long id) {
        return couponDao.getItem(id);
    }


    private void deleteProductRelation(Long id) {
        LambdaQueryWrapper<SmsCouponProductRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsCouponProductRelation::getCouponId,id);
        couponProductRelationMapper.delete(lambdaQueryWrapper);

    }
    private void deleteProductCategoryRelation(Long id) {
        LambdaQueryWrapper<SmsCouponProductCategoryRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsCouponProductCategoryRelation::getCouponId,id);
        productCategoryRelationMapper.delete(lambdaQueryWrapper);
    }

}
