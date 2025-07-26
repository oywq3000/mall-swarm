package com.oyproj.portal.service.impl;
import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.dto.MemberReadHistory;
import com.oyproj.portal.repository.MemberReadHistoryRepository;
import com.oyproj.portal.service.MemberReadHistoryService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author oy
 * @description 会员浏览记录管理Service实现类
 */
@Service
@RequiredArgsConstructor
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {
    private final MemberReadHistoryRepository memberReadHistoryRepository;
    private final UmsMemberService memberService;
    /**
     * 生成浏览记录
     *
     * @param memberReadHistory
     */
    @Override
    public int create(MemberReadHistory memberReadHistory) {
        UmsMember member = memberService.getCurrentMember();
        memberReadHistory.setMemberId(member.getId());
        memberReadHistory.setMemberNickname(member.getNickname());
        memberReadHistory.setMemberIcon(member.getIcon());
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        memberReadHistoryRepository.save(memberReadHistory);
        return 1;
    }

    /**
     * 批量删除浏览记录
     *
     * @param ids
     */
    @Override
    public int delete(List<String> ids) {
        List<MemberReadHistory> deleteList = new ArrayList<>();
        for(String id:ids){
            MemberReadHistory memberReadHistory = new MemberReadHistory();
            memberReadHistory.setId(id);
            deleteList.add(memberReadHistory);
        }
        memberReadHistoryRepository.deleteAll(deleteList);
        return ids.size();
    }

    /**
     * 分页获取用户浏览历史记录
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public Page<MemberReadHistory> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        return memberReadHistoryRepository.findByMemberIdOrderByCreateTimeDesc(member.getId(),pageable);
    }

    /**
     * 清空浏览记录
     */
    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberReadHistoryRepository.deleteAllByMemberId(member.getId());
    }
}
