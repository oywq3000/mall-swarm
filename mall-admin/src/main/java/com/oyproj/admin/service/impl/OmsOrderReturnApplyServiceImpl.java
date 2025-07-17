package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.OmsOrderReturnApplyDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.OmsOrderReturnApplyResult;
import com.oyproj.admin.dto.OmsReturnApplyQueryParam;
import com.oyproj.admin.dto.OmsUpdateStatusParam;
import com.oyproj.admin.mapper.OmsOrderReturnApplyMapper;
import com.oyproj.admin.model.OmsOrderReturnApply;
import com.oyproj.admin.service.OmsOrderReturnApplyService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author oy
 * @description 订单退货管理Service
 */
@Service
@RequiredArgsConstructor
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {

    private final OmsOrderReturnApplyDao returnApplyDao;
    private final OmsOrderReturnApplyMapper returnApplyMapper;

    /**
     * 分页查询申请
     *
     * @param queryParam
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        Page<OmsOrderReturnApply> page1 = new Page<>(pageNum,pageSize);
        Page<OmsOrderReturnApply> page = returnApplyDao.getList(page1, queryParam);
        return PageInfo.build(page);
    }

    /**
     * 批量删除申请
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        LambdaQueryWrapper<OmsOrderReturnApply>  lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OmsOrderReturnApply::getId,ids);
        lambdaQueryWrapper.eq(OmsOrderReturnApply::getStatus,3);//可以删除拒绝退货的商品
        return returnApplyMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 修改申请状态
     *
     * @param id
     * @param statusParam
     */
    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        if(status.equals(1)){
            //确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(2)){
            //完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        }else if(status.equals(3)){
            //拒绝退货
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else{
            return 0;
        }
        return returnApplyMapper.updateById(returnApply);
    }

    /**
     * 获取指定申请详情
     *
     * @param id
     */
    @Override
    public OmsOrderReturnApplyResult getItem(Long id) {
        return returnApplyDao.getDetail(id);
    }
}
