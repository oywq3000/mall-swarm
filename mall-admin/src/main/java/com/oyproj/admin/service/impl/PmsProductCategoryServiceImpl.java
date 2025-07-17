package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.PmsProductCategoryDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.PmsProductCategoryParam;
import com.oyproj.admin.dto.PmsProductCategoryWithChildrenItem;
import com.oyproj.admin.mapper.PmsProductCategoryAttributeRelationMapper;
import com.oyproj.admin.mapper.PmsProductCategoryMapper;
import com.oyproj.admin.mapper.PmsProductMapper;
import com.oyproj.admin.model.PmsProduct;
import com.oyproj.admin.model.PmsProductCategory;
import com.oyproj.admin.model.PmsProductCategoryAttributeRelation;
import com.oyproj.admin.service.PmsProductCategoryService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    private final PmsProductCategoryMapper productCategoryMapper;

    private final PmsProductMapper productMapper;

    private final PmsProductCategoryAttributeRelationMapper productCategoryAttributeRelationMapper;

    private final PmsProductCategoryDao productCategoryDao;
    /**
     * 创建商品分类
     *
     * @param pmsProductCategoryParam
     */
    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryParam,productCategory);
        //没有父分类时为一级分类
        setCategoryLevel(productCategory);
        int count = productCategoryMapper.insert(productCategory);
        //创建筛选属性关联
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return count;
    }

    /**
     * 修改商品分类
     *
     * @param id
     * @param pmsProductCategoryParam
     */
    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);
        setCategoryLevel(productCategory);
        //更新商品分类时要更新商品中的名称
        PmsProduct product = new PmsProduct();
        product.setProductCategoryName(productCategory.getName());
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getProductCategoryId,id);
        productMapper.update(product,lambdaQueryWrapper);
        //同时更新筛选属性的信息
        if(!CollectionUtils.isEmpty(pmsProductCategoryParam.getProductAttributeIdList())){
            LambdaQueryWrapper<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            productCategoryAttributeRelationLambdaQueryWrapper.eq(PmsProductCategoryAttributeRelation::getProductCategoryId,id);
            productCategoryAttributeRelationMapper.delete(productCategoryAttributeRelationLambdaQueryWrapper);
            insertRelationList(id,pmsProductCategoryParam.getProductAttributeIdList());
        }else{
            LambdaQueryWrapper<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            productCategoryAttributeRelationLambdaQueryWrapper.eq(PmsProductCategoryAttributeRelation::getProductCategoryId,id);
            productCategoryAttributeRelationMapper.delete(productCategoryAttributeRelationLambdaQueryWrapper);
        }
        return productCategoryMapper.updateById(productCategory);
    }

    /**
     * 分页获取商品分类
     *
     * @param parentId
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        Page<PmsProductCategory> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProductCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductCategory::getParentId,parentId);
        lambdaQueryWrapper.orderByDesc(PmsProductCategory::getSort);
        Page<PmsProductCategory> page = productCategoryMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);
    }

    /**
     * 删除商品分类
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return productCategoryMapper.deleteById(id);
    }

    /**
     * 根据ID获取商品分类
     *
     * @param id
     */
    @Override
    public PmsProductCategory getItem(Long id) {
        return productCategoryMapper.selectById(id);
    }

    /**
     * 批量修改导航状态
     *
     * @param ids
     * @param navStatus
     */
    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        LambdaQueryWrapper<PmsProductCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProductCategory::getId,ids);
        PmsProductCategory record = new PmsProductCategory();
        record.setNavStatus(navStatus);
        return productCategoryMapper.update(record,lambdaQueryWrapper);
    }

    /**
     * 批量修改显示状态
     *
     * @param ids
     * @param showStatus
     */
    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        LambdaQueryWrapper<PmsProductCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProductCategory::getId,ids);
        PmsProductCategory record = new PmsProductCategory();
        record.setShowStatus(showStatus);
        return productCategoryMapper.update(record,lambdaQueryWrapper);
    }

    /**
     * 以层级形式获取商品分类
     */
    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDao.listWithChildren();
    }

    private void setCategoryLevel(PmsProductCategory productCategory){
        //没有分类时为一级分类
        //没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            //有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory = productCategoryMapper.selectById(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }
    /**
     * 批量插入商品分类与筛选属性关系表
     * @param productCategoryId 商品分类id
     * @param productAttributeIdList 相关商品筛选属性id集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationMapper.insert(relationList);
    }
}
