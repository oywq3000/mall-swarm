package com.oyproj.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.mall.mapper.*;
import com.oyproj.mall.model.*;
import com.oyproj.portal.dao.HomeDao;
import com.oyproj.portal.dto.FlashPromotionProduct;
import com.oyproj.portal.dto.HomeContentResult;
import com.oyproj.portal.dto.HomeFlashPromotion;
import com.oyproj.portal.service.HomeService;
import com.oyproj.portal.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final SmsHomeAdvertiseMapper advertiseMapper;

    private final HomeDao homeDao;

    private final SmsFlashPromotionMapper flashPromotionMapper;

    private final SmsFlashPromotionSessionMapper promotionSessionMapper;

    private final PmsProductMapper productMapper;

    private final PmsProductCategoryMapper productCategoryMapper;

    private final CmsSubjectMapper subjectMapper;

    /**
     * 获取首页内容
     */
    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();

        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        result.setBrandList(homeDao.getRecommendBrandList(new Page<>(0,6)).getRecords());
        //获取秒杀信息
        result.setHomeFlashPromotion(getHomeFlashPromotion());
        //获取新品推荐
        result.setNewProductList(homeDao.getNewProductList(0,4));
        //获取人气推荐
        result.setHotProductList(homeDao.getHotProductList(0,4));
        //获取推荐专题
        result.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return result;
    }

    /**
     * 首页商品推荐
     *
     * @param pageSize
     * @param pageNum
     */
    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO: 2025/7/26 暂时默认推荐所有商品
        Page<PmsProduct> page1= new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getDeleteStatus,0)
                        .eq(PmsProduct::getPublishStatus,1);
        return productMapper.selectPage(page1,lambdaQueryWrapper).getRecords();
    }

    /**
     * 获取商品分类
     *
     * @param parentId 0:获取一级分类；其他：获取指定二级分类
     */
    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        LambdaQueryWrapper<PmsProductCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductCategory::getShowStatus,1)
                .eq(PmsProductCategory::getParentId,parentId);
        lambdaQueryWrapper.orderByDesc(PmsProductCategory::getSort);
        return productCategoryMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 根据专题分类分页获取专题
     *
     * @param cateId   专题分类id
     * @param pageSize
     * @param pageNum
     */
    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        Page<CmsSubject> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<CmsSubject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CmsSubject::getShowStatus,1);
        if(cateId!=null){
            lambdaQueryWrapper.eq(CmsSubject::getCategoryId,cateId);
        }
        return subjectMapper.selectPage(page1,lambdaQueryWrapper).getRecords();
    }

    /**
     * 分页获取人气推荐商品
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeDao.getHotProductList(offset, pageSize);
    }

    /**
     * 分页获取新品推荐商品
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeDao.getNewProductList(offset, pageSize);
    }

    //获取下一个场次信息
    private SmsFlashPromotionSession getNextFlashPromotionSession(Date date) {
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.gt(SmsFlashPromotionSession::getStartTime,date);
        lambdaQueryWrapper.orderByAsc(SmsFlashPromotionSession::getStartTime);

        List<SmsFlashPromotionSession> promotionSessionList = promotionSessionMapper.selectList(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(promotionSessionList)) {
            return promotionSessionList.get(0);
        }
        return null;
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {

        LambdaQueryWrapper<SmsHomeAdvertise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsHomeAdvertise::getType,1).eq(SmsHomeAdvertise::getStatus,1);
        lambdaQueryWrapper.orderByDesc(SmsHomeAdvertise::getSort);

        return advertiseMapper.selectList(lambdaQueryWrapper);
    }

    //根据时间获取秒杀活动
    private SmsFlashPromotion getFlashPromotion(Date date) {
        Date currDate = DateUtil.getDate(date);
        LambdaQueryWrapper<SmsFlashPromotion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotion::getStatus,1)
                .lt(SmsFlashPromotion::getStartDate,currDate)
                .gt(SmsFlashPromotion::getEndDate,currDate);
        List<SmsFlashPromotion> flashPromotionList = flashPromotionMapper.selectList(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(flashPromotionList)) {
            return flashPromotionList.get(0);
        }
        return null;
    }

    //根据时间获取秒杀场次
    private SmsFlashPromotionSession getFlashPromotionSession(Date date) {
        Date currTime = DateUtil.getTime(date);
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(SmsFlashPromotionSession::getStartTime,currTime)
                .gt(SmsFlashPromotionSession::getEndTime,currTime);
        List<SmsFlashPromotionSession> promotionSessionList = promotionSessionMapper.selectList(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(promotionSessionList)) {
            return promotionSessionList.get(0);
        }
        return null;
    }

    private HomeFlashPromotion getHomeFlashPromotion() {
        HomeFlashPromotion homeFlashPromotion = new HomeFlashPromotion();
        //获取当前秒杀活动
        Date now = new Date();
        SmsFlashPromotion flashPromotion = getFlashPromotion(now);
        if (flashPromotion != null) {
            //获取当前秒杀场次
            SmsFlashPromotionSession flashPromotionSession = getFlashPromotionSession(now);
            if (flashPromotionSession != null) {
                homeFlashPromotion.setStartTime(flashPromotionSession.getStartTime());
                homeFlashPromotion.setEndTime(flashPromotionSession.getEndTime());
                //获取下一个秒杀场次
                SmsFlashPromotionSession nextSession = getNextFlashPromotionSession(homeFlashPromotion.getStartTime());
                if(nextSession!=null){
                    homeFlashPromotion.setNextStartTime(nextSession.getStartTime());
                    homeFlashPromotion.setNextEndTime(nextSession.getEndTime());
                }
                //获取秒杀商品
                List<FlashPromotionProduct> flashProductList = homeDao.getFlashProductList(flashPromotion.getId(), flashPromotionSession.getId());
                homeFlashPromotion.setProductList(flashProductList);
            }
        }
        return homeFlashPromotion;
    }
}
