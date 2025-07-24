package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsFlashPromotionMapper;
import com.oyproj.mall.model.SmsFlashPromotion;
import com.oyproj.admin.service.SmsFlashPromotionService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author oy
 * @description 购物活动实现类
 */
@Service
@RequiredArgsConstructor
public class SmsFlashPromotionServiceImpl implements SmsFlashPromotionService {

    private final SmsFlashPromotionMapper flashPromotionMapper;

    /**
     * 添加活动
     *
     * @param flashPromotion
     */
    @Override
    public int create(SmsFlashPromotion flashPromotion) {
        flashPromotion.setCreateTime(new Date());
        return flashPromotionMapper.insert(flashPromotion);
    }

    /**
     * 修改指定活动
     *
     * @param id
     * @param flashPromotion
     */
    @Override
    public int update(Long id, SmsFlashPromotion flashPromotion) {
        flashPromotion.setId(id);
        return flashPromotionMapper.updateById(flashPromotion);
    }

    /**
     * 删除单个活动
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return flashPromotionMapper.deleteById(id);
    }

    /**
     * 修改上下线状态
     *
     * @param id
     * @param status
     */
    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion();
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        return flashPromotionMapper.updateById(flashPromotion);
    }

    /**
     * 获取详细信息
     *
     * @param id
     */
    @Override
    public SmsFlashPromotion getItem(Long id) {
        return flashPromotionMapper.selectById(id);
    }

    /**
     * 分页查询活动
     *
     * @param keyword
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SmsFlashPromotion> page1 = new Page<>(pageNum,pageSize);

        LambdaQueryWrapper<SmsFlashPromotion> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(keyword)) {
            lambdaQueryWrapper.like(SmsFlashPromotion::getTitle,keyword);
        }
        Page<SmsFlashPromotion> page = flashPromotionMapper.selectPage(page1,lambdaQueryWrapper);
        return PageInfo.build(page);
    }
}
