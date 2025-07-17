package com.oyproj.admin.service;

import com.oyproj.admin.model.CmsSubject;
import com.oyproj.common.api.IPageInfo;

import java.util.List;

/**
 * @author oy
 * @description 商品专题管理Service
 */
public interface CmsSubjectService {
    /**
     * 查询所有专题
     */
    List<CmsSubject> listAll();

    /**
     * 分页查询专题
     */
    IPageInfo<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize);
}
