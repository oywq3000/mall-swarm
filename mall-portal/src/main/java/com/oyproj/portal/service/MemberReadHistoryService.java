package com.oyproj.portal.service;

import com.oyproj.portal.dto.MemberReadHistory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author oy
 * @description 会员浏览记录管理Service
 */
public interface MemberReadHistoryService {
    /**
     * 生成浏览记录
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * 批量删除浏览记录
     */
    int delete(List<String> ids);

    /**
     * 分页获取用户浏览历史记录
     */
    Page<MemberReadHistory> list(Integer pageNum, Integer pageSize);

    /**
     * 清空浏览记录
     */
    void clear();
}
