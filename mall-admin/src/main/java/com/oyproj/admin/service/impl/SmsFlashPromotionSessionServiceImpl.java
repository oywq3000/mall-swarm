package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.admin.dto.SmsFlashPromotionSessionDetail;
import com.oyproj.admin.mapper.SmsFlashPromotionSessionMapper;
import com.oyproj.admin.model.SmsFlashPromotionSession;
import com.oyproj.admin.service.SmsFlashPromotionProductRelationService;
import com.oyproj.admin.service.SmsFlashPromotionSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author oy
 * @description 限时购场次管理Service实现类
 */
@Service
@RequiredArgsConstructor
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService {

    private final SmsFlashPromotionSessionMapper promotionSessionMapper;
    private final SmsFlashPromotionProductRelationService relationService;

    /**
     * 添加场次
     *
     * @param promotionSession
     */
    @Override
    public int create(SmsFlashPromotionSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        return promotionSessionMapper.insert(promotionSession);
    }

    /**
     * 修改场次
     *
     * @param id
     * @param promotionSession
     */
    @Override
    public int update(Long id, SmsFlashPromotionSession promotionSession) {
        promotionSession.setId(id);
        return promotionSessionMapper.updateById(promotionSession);
    }

    /**
     * 修改场次启用状态
     *
     * @param id
     * @param status
     */
    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotionSession promotionSession = new SmsFlashPromotionSession();
        promotionSession.setId(id);
        promotionSession.setStatus(status);
        return promotionSessionMapper.updateById(promotionSession);
    }

    /**
     * 删除场次
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
       return promotionSessionMapper.deleteById(id);
    }

    /**
     * 获取详情
     *
     * @param id
     */
    @Override
    public SmsFlashPromotionSession getItem(Long id) {
        return promotionSessionMapper.selectById(id);
    }

    /**
     * 根据启用状态获取场次列表
     */
    @Override
    public List<SmsFlashPromotionSession> list() {
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return promotionSessionMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 获取全部可选场次及其数量
     *
     * @param flashPromotionId
     */
    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {

        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotionSession::getStatus,1);
        List<SmsFlashPromotionSession> flashPromotionSessions = promotionSessionMapper.selectList(lambdaQueryWrapper);
        for (SmsFlashPromotionSession flashPromotionSession : flashPromotionSessions) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(flashPromotionSession, detail);
            long count = relationService.getCount(flashPromotionId, flashPromotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
