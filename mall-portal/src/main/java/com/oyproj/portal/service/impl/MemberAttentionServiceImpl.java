package com.oyproj.portal.service.impl;

import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.dto.MemberBrandAttention;
import com.oyproj.portal.repository.MemberBrandAttentionRepository;
import com.oyproj.portal.service.MemberAttentionService;
import com.oyproj.portal.service.MemberReadHistoryService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberAttentionServiceImpl implements MemberAttentionService {

    private final MemberBrandAttentionRepository memberBrandAttentionRepository;
    private final UmsMemberService memberService;
    /**
     * 添加关注
     *
     * @param memberBrandAttention
     */
    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        int count = 0;
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(member.getId());
        memberBrandAttention.setMemberNickname(member.getNickname());
        memberBrandAttention.setMemberIcon(member.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        MemberBrandAttention findAttention = memberBrandAttentionRepository.findByMemberIdAndBrandId(memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            memberBrandAttentionRepository.save(memberBrandAttention);
            count = 1;
        }
        return count;
    }

    /**
     * 取消关注
     *
     * @param brandId
     */
    @Override
    public int delete(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionRepository.deleteByMemberIdAndBrandId(member.getId(),brandId);
    }

    /**
     * 获取用户关注列表
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return memberBrandAttentionRepository.findByMemberId(member.getId(),pageable);
    }

    /**
     * 获取用户关注详情
     *
     * @param brandId
     */
    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionRepository.findByMemberIdAndBrandId(member.getId(), brandId);
    }

    /**
     * 清空关注列表
     */
    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttentionRepository.deleteAllByMemberId(member.getId());
    }
}
