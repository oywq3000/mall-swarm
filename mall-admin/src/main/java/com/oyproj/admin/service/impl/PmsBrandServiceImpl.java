package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.PmsBrandParam;
import com.oyproj.admin.mapper.PmsBrandMapper;
import com.oyproj.admin.mapper.PmsProductMapper;
import com.oyproj.admin.model.PmsBrand;
import com.oyproj.admin.model.PmsProduct;
import com.oyproj.admin.service.PmsBrandService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author oy
 * @description 品牌管理Service
 */
@Service
@RequiredArgsConstructor
public class PmsBrandServiceImpl implements PmsBrandService {

    private final PmsBrandMapper brandMapper;

    private final PmsProductMapper productMapper;

    /**
     * 获取所有品牌
     */
    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 创建品牌
     *
     * @param pmsBrandParam
     */
    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return brandMapper.insert(pmsBrand);
    }

    /**
     * 修改品牌
     *
     * @param id
     * @param pmsBrandParam
     */
    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        //更新品牌时要更新商品中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getBrandId,id);
        productMapper.update(product,lambdaQueryWrapper);
        return brandMapper.updateById(pmsBrand);
    }

    /**
     * 删除品牌
     *
     * @param id
     */
    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteById(id);
    }

    /**
     * 批量删除品牌
     *
     * @param ids
     */
    @Override
    public int deleteBrand(List<Long> ids) {
        LambdaQueryWrapper<PmsBrand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsBrand::getId,ids);
        return brandMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 分页查询品牌
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     */
    @Override
    public IPageInfo<PmsBrand> listBrand(String keyword, int pageNum, int pageSize) {
        Page<PmsBrand> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsBrand> lambdaQueryWrapper =new LambdaQueryWrapper<>();

        if(StringUtils.hasText(keyword)){
            lambdaQueryWrapper.like(PmsBrand::getName,keyword);
        }
        lambdaQueryWrapper.orderByDesc(PmsBrand::getSort);
        Page<PmsBrand> page = brandMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }

    /**
     * 获取品牌
     *
     * @param id
     */
    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectById(id);
    }

    /**
     * 修改显示状态
     *
     * @param ids
     * @param showStatus
     */
    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        LambdaQueryWrapper<PmsBrand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsBrand::getId,ids);
        return brandMapper.update(pmsBrand, lambdaQueryWrapper);
    }

    /**
     * 修改厂家制造商状态
     *
     * @param ids
     * @param factoryStatus
     */
    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);
        LambdaQueryWrapper<PmsBrand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsBrand::getId,ids);
        return brandMapper.update(pmsBrand, lambdaQueryWrapper);
    }
}
