package com.oyproj.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.mall.mapper.*;
import com.oyproj.mall.model.*;
import com.oyproj.portal.dao.PortalProductDao;
import com.oyproj.portal.domain.PageInfo;
import com.oyproj.portal.dto.PmsPortalProductDetail;
import com.oyproj.portal.dto.PmsProductCategoryNode;
import com.oyproj.portal.service.PmsPortalProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PmsPortalProductServiceImpl implements PmsPortalProductService {

    private final PmsProductMapper productMapper;

    private final PmsProductCategoryMapper productCategoryMapper;

    private final PmsBrandMapper brandMapper;

    private final PmsProductAttributeMapper productAttributeMapper;

    private final PmsProductAttributeValueMapper productAttributeValueMapper;

    private final PmsSkuStockMapper skuStockMapper;

    private final PmsProductLadderMapper productLadderMapper;

    private final PmsProductFullReductionMapper productFullReductionMapper;

    private final PortalProductDao portalProductDao;

    /**
     * 综合搜索商品
     *
     * @param keyword
     * @param brandId
     * @param productCategoryId
     * @param pageNum
     * @param pageSize
     * @param sort
     */
    @Override
    public IPageInfo<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        Page<PmsProduct> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getDeleteStatus,0);
        if (StrUtil.isNotEmpty(keyword)) {
            lambdaQueryWrapper.like(PmsProduct::getName,keyword);
        }
        if (brandId != null) {
            lambdaQueryWrapper.eq(PmsProduct::getBrandId,brandId);
        }
        if (productCategoryId != null) {
            lambdaQueryWrapper.eq(PmsProduct::getProductCategoryId,productCategoryId);
        }
        //1->按新品；2->按销量；3->价格从低到高；4->价格从高到低
        if (sort == 1) {
            lambdaQueryWrapper.orderByDesc(PmsProduct::getId);
        } else if (sort == 2) {
            lambdaQueryWrapper.orderByDesc(PmsProduct::getSale);
        } else if (sort == 3) {
            lambdaQueryWrapper.orderByAsc(PmsProduct::getPrice);
        } else if (sort == 4) {
            lambdaQueryWrapper.orderByDesc(PmsProduct::getPrice);
        }
        Page<PmsProduct> page = productMapper.selectPage(page1, lambdaQueryWrapper);

        return PageInfo.build(page) ;
    }

    /**
     * 以树形结构获取所有商品分类
     */
    @Override
    public List<PmsProductCategoryNode> categoryTreeList() {
        List<PmsProductCategory> pmsProductCategories = productCategoryMapper.selectList(new LambdaQueryWrapper<>());
        List<PmsProductCategoryNode> pmsProductCategoryNodes = pmsProductCategories.stream()
                .filter(item -> item.getProductCount().equals(0L))
                .map(item -> covert(item, pmsProductCategories)).toList();
        return pmsProductCategoryNodes;
    }

    /**
     * 获取前台商品详情
     *
     * @param id
     */
    @Override
    public PmsPortalProductDetail detail(Long id) {
        PmsPortalProductDetail result = new PmsPortalProductDetail();
        //获取商品信息
        PmsProduct pmsProduct = productMapper.selectById(id);
        result.setProduct(pmsProduct);
        //获取品牌信息
        PmsBrand pmsBrand = brandMapper.selectById(pmsProduct.getBrandId());
        result.setBrand(pmsBrand);
        //获取商品属性信息
        LambdaQueryWrapper<PmsProductAttribute> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductAttribute::getProductAttributeCategoryId,pmsProduct.getProductAttributeCategoryId());
        List<PmsProductAttribute> pmsProductAttributes = productAttributeMapper.selectList(lambdaQueryWrapper);
        result.setProductAttributeList(pmsProductAttributes);
        //获取商品属性值信息
        if(CollUtil.isNotEmpty(pmsProductAttributes)){
            List<Long> attributeIds = pmsProductAttributes.stream().map(PmsProductAttribute::getId).toList();
            LambdaQueryWrapper<PmsProductAttributeValue> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(PmsProductAttributeValue::getProductId,pmsProduct.getId())
                    .in(PmsProductAttributeValue::getProductAttributeId,attributeIds);
            List<PmsProductAttributeValue> pmsProductAttributeValues = productAttributeValueMapper.selectList(lambdaQueryWrapper1);
            result.setProductAttributeValueList(pmsProductAttributeValues);
        }
        //获取商品SKU库存信息
        LambdaQueryWrapper<PmsSkuStock> skuStockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        skuStockLambdaQueryWrapper.eq(PmsSkuStock::getProductId,pmsProduct.getId());
        List<PmsSkuStock> pmsSkuStocks = skuStockMapper.selectList(skuStockLambdaQueryWrapper);
        result.setSkuStockList(pmsSkuStocks);
        //商品阶梯价格设置
        if(pmsProduct.getPromotionType()==3){
            LambdaQueryWrapper<PmsProductLadder> ladderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ladderLambdaQueryWrapper.eq(PmsProductLadder::getProductId,pmsProduct.getId());
            List<PmsProductLadder> pmsProductLadders = productLadderMapper.selectList(ladderLambdaQueryWrapper);
            result.setProductLadderList(pmsProductLadders);
        }
        //商品满减价格设置
        if(pmsProduct.getPromotionType()==4){
            LambdaQueryWrapper<PmsProductFullReduction> reductionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reductionLambdaQueryWrapper.eq(PmsProductFullReduction::getProductId,pmsProduct.getId());
            List<PmsProductFullReduction> pmsProductFullReductions = productFullReductionMapper.selectList(reductionLambdaQueryWrapper);
            result.setProductFullReductionList(pmsProductFullReductions);
        }
        result.setCouponList(portalProductDao.getAvailableCouponList(pmsProduct.getId(),pmsProduct.getProductCategoryId()));
        return result;
    }
    /**
     * 初始对象转化为节点对象
     */
    private PmsProductCategoryNode covert(PmsProductCategory item, List<PmsProductCategory> allList) {
        PmsProductCategoryNode node = new PmsProductCategoryNode();
        BeanUtils.copyProperties(item, node);
        List<PmsProductCategoryNode> children = allList.stream()
                .filter(subItem -> subItem.getParentId().equals(item.getId()))
                .map(subItem -> covert(subItem, allList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
