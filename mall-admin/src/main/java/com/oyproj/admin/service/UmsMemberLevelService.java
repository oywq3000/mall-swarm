package com.oyproj.admin.service;

import com.oyproj.admin.model.UmsMemberLevel;

import java.util.List;

/**
 * @author oy
 * @description 会员等级管理Service
 */
public interface UmsMemberLevelService {
    /**
     * 获取所有会员登入
     * @param defaultStatus 是否为默认会员
     * @return
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
