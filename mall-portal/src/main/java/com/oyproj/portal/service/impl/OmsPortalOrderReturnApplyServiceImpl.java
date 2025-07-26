package com.oyproj.portal.service.impl;

import com.oyproj.mall.mapper.OmsOrderReturnApplyMapper;
import com.oyproj.mall.model.OmsOrderReturnApply;
import com.oyproj.portal.dto.OmsOrderReturnApplyParam;
import com.oyproj.portal.service.OmsPortalOrderReturnApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    private final OmsOrderReturnApplyMapper returnApplyMapper;
    /**
     * 提交申请
     *
     * @param returnApplyParam
     */
    @Override
    public int create(OmsOrderReturnApplyParam returnApplyParam) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApplyParam,realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        return returnApplyMapper.insert(realApply);
    }
}
