package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.admin.mapper.UmsMemberLevelMapper;
import com.oyproj.admin.model.UmsMemberLevel;
import com.oyproj.admin.service.UmsMemberLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UmsMemberLevelImpl implements UmsMemberLevelService {

    private final UmsMemberLevelMapper memberLevelMapper;
    /**
     * 获取所有会员登入
     *
     * @param defaultStatus 是否为默认会员
     * @return
     */
    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        LambdaQueryWrapper<UmsMemberLevel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberLevel::getDefaultStatus,defaultStatus);
        return memberLevelMapper.selectList(lambdaQueryWrapper);
    }
}
