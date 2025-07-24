package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.mall.mapper.OmsOrderReturnReasonMapper;
import com.oyproj.mall.model.OmsOrderReturnReason;
import com.oyproj.admin.service.OmsOrderReturnReasonService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {

    private final OmsOrderReturnReasonMapper omsOrderReturnReasonMapper;

    /**
     * 添加订单原因
     *
     * @param returnReason
     */
    @Override
    public int create(OmsOrderReturnReason returnReason) {
        returnReason.setCreateTime(new Date());
        return omsOrderReturnReasonMapper.insert(returnReason);
    }

    /**
     * 修改退货原因
     *
     * @param id
     * @param returnReason
     */
    @Override
    public int update(Long id, OmsOrderReturnReason returnReason) {
        returnReason.setId(id);
        return omsOrderReturnReasonMapper.updateById(returnReason);
    }

    /**
     * 批量删除退货原因
     *
     * @param ids
     */
    @Override
    public int delete(List<Long> ids) {
        LambdaQueryWrapper<OmsOrderReturnReason> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OmsOrderReturnReason::getId,ids);
        return omsOrderReturnReasonMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 分页获取退货原因
     *
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum) {
        Page<OmsOrderReturnReason> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<OmsOrderReturnReason> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(OmsOrderReturnReason::getSort);
        Page<OmsOrderReturnReason> page = omsOrderReturnReasonMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }

    /**
     * 批量修改退货原因状态
     *
     * @param ids
     * @param status
     */
    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        if(!status.equals(0)&&!status.equals(1)){
            return 0;
        }
        OmsOrderReturnReason record = new OmsOrderReturnReason();
        record.setStatus(status);
        LambdaQueryWrapper<OmsOrderReturnReason> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OmsOrderReturnReason::getId,ids);
        return omsOrderReturnReasonMapper.update(record,lambdaQueryWrapper);
    }

    /**
     * 获取单个退货原因详情信息
     *
     * @param id
     */
    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return omsOrderReturnReasonMapper.selectById(id);
    }
}
