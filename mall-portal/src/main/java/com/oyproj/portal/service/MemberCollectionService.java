package com.oyproj.portal.service;

import com.oyproj.portal.dto.MemberProductCollection;
import org.springframework.data.domain.Page;

/**
 * @author oy
 * @description 会员收藏Service
 */
public interface MemberCollectionService {
    int add(MemberProductCollection productCollection);

    int delete(Long productId);

    Page<MemberProductCollection> list(Integer pageNum, Integer pageSize);

    MemberProductCollection detail(Long productId);

    void clear();
}
