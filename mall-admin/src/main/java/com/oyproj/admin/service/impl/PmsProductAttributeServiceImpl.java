package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.PmsProductAttributeDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.PmsProductAttributeParam;
import com.oyproj.admin.dto.ProductAttrInfo;
import com.oyproj.mall.mapper.PmsProductAttributeCategoryMapper;
import com.oyproj.mall.mapper.PmsProductAttributeMapper;
import com.oyproj.mall.model.PmsProductAttribute;
import com.oyproj.mall.model.PmsProductAttributeCategory;
import com.oyproj.admin.service.PmsProductAttributeService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    private final PmsProductAttributeMapper productAttributeMapper;

    private final PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    private final PmsProductAttributeDao productAttributeDao;

    /**
     * 根据分类分页获取商品属性
     *
     * @param cid      分类id
     * @param type     0->属性；2->参数
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        Page<PmsProductAttribute> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProductAttribute> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductAttribute::getProductAttributeCategoryId,cid);
        lambdaQueryWrapper.eq(PmsProductAttribute::getType,type);
        Page<PmsProductAttribute> page = productAttributeMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }

    /**
     * 添加商品属性
     *
     * @param pmsProductAttributeParam
     */
    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        PmsProductAttribute pmsProductAttribute  = new PmsProductAttribute();
        BeanUtils.copyProperties(pmsProductAttributeParam,pmsProductAttribute);
        int count = productAttributeMapper.insert(pmsProductAttribute);
        //新增商品属性以后需要更新商品属性分类数量
        PmsProductAttributeCategory pmsProductAttributeCategory
                = productAttributeCategoryMapper.selectById(pmsProductAttribute.getProductAttributeCategoryId());
        if(pmsProductAttribute.getType()==0){
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttribute.getType());
        } else if(pmsProductAttribute.getType()==1){
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount()+1);
        }
        productAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return count;
    }

    /**
     * 修改商品属性
     *
     * @param id
     * @param productAttributeParam
     */
    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        pmsProductAttribute.setId(id);
        BeanUtils.copyProperties(productAttributeParam,pmsProductAttribute);
        return productAttributeMapper.updateById(pmsProductAttribute);
    }

    /**
     * 获取单个商品属性信息
     *
     * @param id
     */
    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeMapper.selectById(id);
    }

    @Override
    public int delete(List<Long> ids) {
        //获取分类
        PmsProductAttribute pmsProductAttribute = productAttributeMapper.selectById(ids.get(0));
        Integer type = pmsProductAttribute.getType();
        PmsProductAttributeCategory pmsProductAttributeCategory = productAttributeCategoryMapper.selectById(pmsProductAttribute.getProductAttributeCategoryId());
        LambdaQueryWrapper<PmsProductAttribute> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProductAttribute::getId,ids);
        int count = productAttributeMapper.delete(lambdaQueryWrapper);

        if(type==0){ //规格
            if(pmsProductAttributeCategory.getAttributeCount()>=count){
                pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount()-count);
            }else {
                pmsProductAttributeCategory.setAttributeCount(0);
            }
        }else if(type==1){ //参数
            if(pmsProductAttributeCategory.getParamCount()>=count){
                pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount()-count);
            }else{
                pmsProductAttributeCategory.setParamCount(0);
            }
        }
        productAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return productAttributeDao.getProductAttrInfo(productCategoryId);
    }
}
