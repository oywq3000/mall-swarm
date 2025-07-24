package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.SmsHomeRecommendSubjectMapper;
import com.oyproj.mall.model.SmsHomeRecommendSubject;
import com.oyproj.admin.service.SmsHomeRecommendSubjectService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsHomeRecommendSubjectServiceImpl implements SmsHomeRecommendSubjectService {

    private final SmsHomeRecommendSubjectMapper recommendProductMapper;
    /**
     * 添加首页推荐
     *
     * @param recommendSubjectList
     */
    @Override
    public int create(List<SmsHomeRecommendSubject> recommendSubjectList) {
        for (SmsHomeRecommendSubject smsHomeRecommendSubject : recommendSubjectList) {
            smsHomeRecommendSubject.setRecommendStatus(1);
            smsHomeRecommendSubject.setSort(0);
        }
        recommendProductMapper.insert(recommendSubjectList);
        return recommendSubjectList.size();
    }

    /**
     * 修改推荐排序
     *
     * @param id
     * @param sort
     */
    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject recommendProduct = new SmsHomeRecommendSubject();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return recommendProductMapper.updateById(recommendProduct);
    }

    /**
     * 批量删除推荐
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        return recommendProductMapper.deleteByIds(ids);
    }

    /**
     * 更新推荐状态
     *
     * @param ids
     * @param recommendStatus
     */
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaQueryWrapper<SmsHomeRecommendSubject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SmsHomeRecommendSubject::getId,ids);
        //创建需要更新的实体对象
        SmsHomeRecommendSubject updateEntity = new SmsHomeRecommendSubject();
        updateEntity.setRecommendStatus(recommendStatus);

        //执行更新操作
        return recommendProductMapper.update(updateEntity,lambdaQueryWrapper);
    }

    /**
     * 分页查询推荐
     *
     * @param subjectName
     * @param recommendStatus
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeRecommendSubject> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SmsHomeRecommendSubject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(subjectName)){
            lambdaQueryWrapper.like(SmsHomeRecommendSubject::getSubjectName,subjectName);
        }
        if(recommendStatus!=null){
            lambdaQueryWrapper.eq(SmsHomeRecommendSubject::getRecommendStatus,recommendStatus);
        }
        lambdaQueryWrapper.orderByDesc(SmsHomeRecommendSubject::getSort);
        PageInfo<SmsHomeRecommendSubject> pageInfo = PageInfo.build(recommendProductMapper.selectPage(page1, lambdaQueryWrapper));
        return pageInfo;
    }
}
