package com.oyproj.portal.service.impl;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.common.exception.Asserts;

import com.oyproj.mall.mapper.*;
import com.oyproj.mall.model.*;
import com.oyproj.portal.dao.SmsCouponHistoryDao;
import com.oyproj.portal.dto.CartPromotionItem;
import com.oyproj.portal.dto.SmsCouponHistoryDetail;
import com.oyproj.portal.service.UmsMemberCouponService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    private final UmsMemberService memberService;

    private final SmsCouponMapper couponMapper;

    private final SmsCouponHistoryMapper couponHistoryMapper;

    private final SmsCouponHistoryDao couponHistoryDao;

    private final SmsCouponProductRelationMapper couponProductRelationMapper;

    private final SmsCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;

    private final PmsProductMapper productMapper;

    /**
     * 会员添加优惠卷
     *
     * @param couponId
     */
    @Override
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        //获取优惠卷信息,并判断数量
        SmsCoupon coupon = couponMapper.selectById(couponId);
        if(coupon==null){
            Asserts.fail("优惠卷不存在");
        }
        if(coupon.getCount()<=0){
            Asserts.fail("优惠券已经领完了");
        }
        Date now = new Date();
        if(now.before(coupon.getEnableTime())){
            Asserts.fail("优惠卷还没有到领取时间");
        }
        //判断用户领取的优惠卷数量是否超过限制
        LambdaQueryWrapper<SmsCouponHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsCouponHistory::getCouponId,couponId)
                .eq(SmsCouponHistory::getMemberId,currentMember.getId());
        Long count = couponHistoryMapper.selectCount(lambdaQueryWrapper);
        if(count>=coupon.getPerLimit()){
            Asserts.fail("您已经领取过该优惠券");
        }
        //生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        //主动领取
        couponHistory.setGetType(1);
        //未使用
        couponHistory.setUseStatus(0);
        couponHistoryMapper.insert(couponHistory);
        //修改优惠卷表的数量，领取数量
        coupon.setCount(coupon.getCount()-1);
        coupon.setReceiveCount(coupon.getReceiveCount()==null?1:coupon.getReceiveCount()+1);
        couponMapper.updateById(coupon);
    }
    /**
     * 16位优惠卷码生成：时间截后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId){
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
    }

    /**
     * 获取优惠卷历史表
     *
     * @param useStatus
     */
    @Override
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<SmsCouponHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsCouponHistory::getMemberId,currentMember.getId());
        if(useStatus!=null){
            lambdaQueryWrapper.eq(SmsCouponHistory::getUseStatus,useStatus);
        }
        return couponHistoryMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 根据购物车信息获取可用优惠券
     *
     * @param cartItemList
     * @param type
     */
    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        Date now = new Date();
        //获取该用户所有优惠卷
        List<SmsCouponHistoryDetail> allList = couponHistoryDao.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if(useType.equals(0)){
                //0->全场通用
                //判断是否满足优惠卷起点
                //计算购物车商品的总价
                BigDecimal totalAmount  = calcTotalAmount(cartItemList);
                if(now.before(endTime)&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
            else if (useType.equals(1)){
                //1->指定分类
                //计算指定分类产品的总价值
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductCategoryId(cartItemList,productCategoryIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
            else if(useType.equals(2)){
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList,productIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if(type.equals(1)){
            return enableList;
        }else{
            return disableList;
        }
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList){
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartItemList) {
            BigDecimal realPrice = cartPromotionItem.getPrice().subtract(cartPromotionItem.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductCategoryId(List<CartPromotionItem> cartItemList,List<Long> productCategoryIds){
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList,List<Long> productIds){
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if(productIds.contains(item.getProductId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    /**
     * 获取当前商品相关优惠券
     *
     * @param productId
     */
    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        List<Long> allCouponIds = new ArrayList<>();
        //获取指定商品优惠卷
        LambdaQueryWrapper<SmsCouponProductRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsCouponProductRelation::getProductId,productId);
        List<SmsCouponProductRelation> cprList = couponProductRelationMapper.selectList(lambdaQueryWrapper);
        if(CollUtil.isNotEmpty(cprList)){
            List<Long> couponIds = cprList.stream().map(SmsCouponProductRelation::getCouponId).toList();
            allCouponIds.addAll(couponIds);
        }
        //获取指定分类优惠卷
        PmsProduct product = productMapper.selectById(productId);
        LambdaQueryWrapper<SmsCouponProductCategoryRelation> relationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        relationLambdaQueryWrapper.eq(SmsCouponProductCategoryRelation::getProductCategoryId,product.getProductCategoryId());
        List<SmsCouponProductCategoryRelation> cpcrList = couponProductCategoryRelationMapper.selectList(relationLambdaQueryWrapper);
        if(CollUtil.isNotEmpty(cpcrList)){
            List<Long> couponIds = cpcrList.stream().map(SmsCouponProductCategoryRelation::getCouponId).toList();
            allCouponIds.addAll(couponIds);
        }
        if(CollUtil.isEmpty(allCouponIds)){
            return new ArrayList<>();
        }
        //所有优惠卷
        LambdaQueryWrapper<SmsCoupon>  couponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponLambdaQueryWrapper.gt(SmsCoupon::getEnableTime,new Date())
                .lt(SmsCoupon::getStartTime,new Date())
                .eq(SmsCoupon::getUseType,0);
        couponLambdaQueryWrapper.or()
                .gt(SmsCoupon::getEnableTime,new Date())
                .lt(SmsCoupon::getStartTime,new Date())
                .ne(SmsCoupon::getUseType,0)
                .in(SmsCoupon::getId,allCouponIds);
        return couponMapper.selectList(couponLambdaQueryWrapper);
    }

    /**
     * 获取用户优惠券列表
     *
     * @param useStatus
     */
    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UmsMember member = memberService.getCurrentMember();
        return couponHistoryDao.getCouponList(member.getId(),useStatus);
    }
}
