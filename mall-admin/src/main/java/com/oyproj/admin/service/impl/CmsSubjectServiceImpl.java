package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.CmsSubjectMapper;
import com.oyproj.mall.model.CmsSubject;
import com.oyproj.admin.service.CmsSubjectService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CmsSubjectServiceImpl implements CmsSubjectService {

    private final CmsSubjectMapper subjectMapper;

    /**
     * 查询所有专题
     */
    @Override
    public List<CmsSubject> listAll() {
        return subjectMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 分页查询专题
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     */
    @Override
    public IPageInfo<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        Page<CmsSubject> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<CmsSubject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(keyword)){
            lambdaQueryWrapper.like(CmsSubject::getTitle,keyword);
        }
        Page<CmsSubject> cmsSubjectPage = subjectMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(cmsSubjectPage);
    }
}
