package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.SmsFlashPromotionProductRelationDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.SmsFlashPromotionProduct;
import com.oyproj.mall.mapper.SmsFlashPromotionProductRelationMapper;
import com.oyproj.mall.model.SmsFlashPromotionProductRelation;
import com.oyproj.admin.service.SmsFlashPromotionProductRelationService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsFlashPromotionProductRelationServiceImpl implements SmsFlashPromotionProductRelationService {
    private final SmsFlashPromotionProductRelationMapper relationMapper;
    private final SmsFlashPromotionProductRelationDao relationDao;
    /**
     * 批量添加关联
     *
     * @param relationList
     */
    @Override
    public int create(List<SmsFlashPromotionProductRelation> relationList) {
        relationMapper.insert(relationList);
        return relationList.size();
    }

    /**
     * 修改关联相关信息
     *
     * @param id
     * @param relation
     */
    @Override
    public int update(Long id, SmsFlashPromotionProductRelation relation) {
        relation.setId(id);
        return relationMapper.updateById(relation);
    }

    /**
     * 删除关联
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return relationMapper.deleteById(id);
    }

    /**
     * 获取关联详情
     *
     * @param id
     */
    @Override
    public SmsFlashPromotionProductRelation getItem(Long id) {
        return relationMapper.selectById(id);
    }

    /**
     * 分页查询相关商品及促销信息
     *
     * @param flashPromotionId        限时购id
     * @param flashPromotionSessionId 限时购场次id
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        Page<SmsFlashPromotionProduct> page1 = new Page<>(pageNum,pageSize);
        Page<SmsFlashPromotionProduct> page = relationDao.getList(page1, flashPromotionId, flashPromotionSessionId);
        return PageInfo.build(page); //todo test
    }

    /**
     * 根据活动和场次id获取商品关系数量
     *
     * @param flashPromotionId
     * @param flashPromotionSessionId
     * @return
     */
    @Override
    public long getCount(Long flashPromotionId, Long flashPromotionSessionId) {
        LambdaQueryWrapper<SmsFlashPromotionProductRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotionProductRelation::getFlashPromotionId,flashPromotionId);
        lambdaQueryWrapper.eq(SmsFlashPromotionProductRelation::getFlashPromotionSessionId,flashPromotionSessionId);
        return relationMapper.selectCount(lambdaQueryWrapper);
    }
}
