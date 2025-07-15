package com.oyproj.admin.service.impl;

import com.oyproj.admin.dto.SmsFlashPromotionSessionDetail;
import com.oyproj.admin.mapper.SmsFlashPromotionSessionMapper;
import com.oyproj.admin.model.SmsFlashPromotionSession;
import com.oyproj.admin.service.SmsFlashPromotionProductRelationService;
import com.oyproj.admin.service.SmsFlashPromotionSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return 0;
    }

    /**
     * 修改场次
     *
     * @param id
     * @param promotionSession
     */
    @Override
    public int update(Long id, SmsFlashPromotionSession promotionSession) {
        return 0;
    }

    /**
     * 修改场次启用状态
     *
     * @param id
     * @param status
     */
    @Override
    public int updateStatus(Long id, Integer status) {
        return 0;
    }

    /**
     * 删除场次
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return 0;
    }

    /**
     * 获取详情
     *
     * @param id
     */
    @Override
    public SmsFlashPromotionSession getItem(Long id) {
        return null;
    }

    /**
     * 根据启用状态获取场次列表
     */
    @Override
    public List<SmsFlashPromotionSession> list() {
        return null;
    }

    /**
     * 获取全部可选场次及其数量
     *
     * @param flashPromotionId
     */
    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        return null;
    }
}
