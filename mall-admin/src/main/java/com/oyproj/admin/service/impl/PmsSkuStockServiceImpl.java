package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.admin.dao.PmsSkuStockDao;
import com.oyproj.mall.mapper.PmsSkuStockMapper;
import com.oyproj.mall.model.PmsSkuStock;
import com.oyproj.admin.service.PmsSkuStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    private final PmsSkuStockMapper skuStockMapper;
    private final PmsSkuStockDao skuStockDao;
    /**
     * 根据产品id和skuCode模糊搜索
     *
     * @param pid
     * @param keyword
     */
    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        LambdaQueryWrapper<PmsSkuStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsSkuStock::getId,pid);
        if (!StringUtils.isEmpty(keyword)) {
            lambdaQueryWrapper.like(PmsSkuStock::getSkuCode,keyword);
        }
        return skuStockMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 批量更新商品库存信息
     *
     * @param pid
     * @param skuStockList
     */
    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return skuStockDao.replaceList(skuStockList);
    }
}
