package com.oyproj.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.oyproj.mall.mapper.SmsNoticeMapper;
import com.oyproj.mall.model.SmsNotice;
import com.oyproj.portal.dto.SmsNoticeDto;
import com.oyproj.portal.service.SmsNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SmsNoticeServiceImpl implements SmsNoticeService {
    private final SmsNoticeMapper noticeMapper;
    @Override
    public List<SmsNotice> list() {
        List<SmsNotice> smsNotices = noticeMapper.selectList(new LambdaQueryWrapper<>());
        return smsNotices;
    }
}
