package com.oyproj.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.mapper.PmsProductMapper;
import com.oyproj.mall.mapper.PmsSearchHistoryMapper;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.mall.model.PmsSearchHistory;
import com.oyproj.portal.domain.PageInfo;
import com.oyproj.portal.service.PmsSearchService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsSearchServiceImpl implements PmsSearchService {

    private final PmsProductMapper productMapper;
    private final PmsSearchHistoryMapper searchHistoryMapper;
    private final UmsMemberService memberService;
    @Override
    public IPageInfo<PmsProduct> search(String keyword, Integer pageNum, Integer pageSize, Integer sort) {
        Page<PmsProduct> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(PmsProduct::getDeleteStatus,0);
        if(StringUtils.hasText(keyword)){
            lambdaQueryWrapper.and(wrapper->{
                wrapper.like(PmsProduct::getName,keyword)
                        .or()
                        .like(PmsProduct::getBrandName,keyword)
                        .or()
                        .like(PmsProduct::getKeywords,keyword)
                        .or()
                        .like(PmsProduct::getProductSn,keyword);
            });
        }
        Page<PmsProduct> page = productMapper.selectPage(page1, lambdaQueryWrapper);
        //保存搜索历史
        storeSearchHistory(keyword);
        return PageInfo.build(page);
    }

    @Override
    public void storeSearchHistory(String keyword) {
        //先查找该关键词是否在记录内
        LambdaQueryWrapper<PmsSearchHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsSearchHistory::getKeyword,keyword);
        List<PmsSearchHistory> pmsSearchHistories = searchHistoryMapper.selectList(lambdaQueryWrapper);
        if(pmsSearchHistories.size()==0){
            //插入
            PmsSearchHistory pmsSearchHistory = new PmsSearchHistory();
            pmsSearchHistory.setKeyword(keyword);
            pmsSearchHistory.setMemberId(memberService.getCurrentMember().getId());
            pmsSearchHistory.setCreateTime(new Date());
            searchHistoryMapper.insert(pmsSearchHistory);
        }else{
            //更新时间
            PmsSearchHistory pmsSearchHistory = pmsSearchHistories.get(0);
            pmsSearchHistory.setCreateTime(new Date());
            searchHistoryMapper.updateById(pmsSearchHistory);
        }
    }

    @Override
    public int dropSearchHistory(List<Long> ids){
        return searchHistoryMapper.deleteByIds(ids);
    }

    @Override
    public List<PmsSearchHistory> getSearchHistory() {
        Long memberId = memberService.getCurrentMember().getId();
        if(memberId==null){
            return null;
        }else{
            LambdaQueryWrapper<PmsSearchHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PmsSearchHistory::getMemberId,memberId)
                    .orderByDesc(PmsSearchHistory::getCreateTime);
            Page<PmsSearchHistory> page = new Page<>(1,5);//返回最前面5个记录
            return searchHistoryMapper.selectPage(page,lambdaQueryWrapper).getRecords();
        }
    }
}
