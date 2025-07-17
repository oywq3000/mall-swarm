package com.oyproj.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.PmsProductDao;
import com.oyproj.admin.dao.PmsSkuStockDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.PmsProductParam;
import com.oyproj.admin.dto.PmsProductQueryParam;
import com.oyproj.admin.dto.PmsProductResult;
import com.oyproj.admin.mapper.*;
import com.oyproj.admin.model.*;
import com.oyproj.admin.service.PmsProductService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oy
 * @description 商品管理Service实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PmsProductServiceImpl implements PmsProductService {
    private final PmsProductMapper productMapper;

    private final PmsMemberPriceMapper memberPriceMapper;


    private final PmsProductLadderMapper productLadderMapper;

    private final PmsProductFullReductionMapper productFullReductionMapper;

    private final PmsSkuStockDao skuStockDao;

    private final PmsSkuStockMapper skuStockMapper;

    private final PmsProductAttributeValueMapper productAttributeValueMapper;

    private final CmsSubjectProductRelationMapper subjectProductRelationMapper;

    private final CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;

    private final PmsProductDao productDao;

    private final PmsProductVerifyRecordMapper pmsProductVerifyRecordMapper;

    /**
     * 创建商品
     *
     * @param productParam
     */
    @Override
    public int create(PmsProductParam productParam) {
        int count = 0;
        //创建商品
        PmsProduct product = productParam;
        product.setId(null);
        productMapper.insert(product);
        //根据促销类型设置价格：会员价格、阶梯价格、满减价格
        Long productId = product.getId();
        //会员价格
        relateAndInsertList(memberPriceMapper, productParam.getMemberPriceList(), productId);
        //阶梯价格
        relateAndInsertList(productLadderMapper, productParam.getProductLadderList(), productId);
        //满减价格
        relateAndInsertList(productFullReductionMapper, productParam.getProductFullReductionList(), productId);
        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        //添加sku库存信息
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueMapper, productParam.getProductAttributeValueList(), productId);
        //关联专题
        relateAndInsertList(subjectProductRelationMapper, productParam.getSubjectProductRelationList(), productId);
        //关联优选
        relateAndInsertList(prefrenceAreaProductRelationMapper, productParam.getPrefrenceAreaProductRelationList(), productId);
        count = 1;
        return count;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) return;
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    /**
     * 根据商品编号获取更新信息
     *
     * @param id
     */
    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productDao.getUpdateInfo(id);
    }

    /**
     * 更新商品
     *
     * @param id
     * @param productParam
     */
    @Override
    public int update(Long id, PmsProductParam productParam) {
        int count;
        //更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        productMapper.insert(product);
        //会员价格
        LambdaQueryWrapper<PmsMemberPrice> memberPriceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberPriceLambdaQueryWrapper.eq(PmsMemberPrice::getProductId, id);
        memberPriceMapper.delete(memberPriceLambdaQueryWrapper);
        relateAndInsertList(memberPriceMapper, productParam.getMemberPriceList(), id);
        //阶梯价格
        LambdaQueryWrapper<PmsProductLadder> pmsProductLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmsProductLambdaQueryWrapper.eq(PmsProductLadder::getProductId, id);
        productLadderMapper.delete(pmsProductLambdaQueryWrapper);
        relateAndInsertList(productLadderMapper, productParam.getProductLadderList(), id);
        //满减价格
        LambdaQueryWrapper<PmsProductFullReduction> pmsProductFullReductionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmsProductFullReductionLambdaQueryWrapper.eq(PmsProductFullReduction::getProductId, id);
        productFullReductionMapper.delete(pmsProductFullReductionLambdaQueryWrapper);
        relateAndInsertList(productFullReductionMapper, productParam.getProductFullReductionList(), id);
        //修改sku库存信息
        handleUpdateSkuStockList(id, productParam);
        //修改商品参数,添加自定义商品规格
        LambdaQueryWrapper<PmsProductAttributeValue> pmsProductAttributeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmsProductAttributeValueLambdaQueryWrapper.eq(PmsProductAttributeValue::getProductId, id);
        productAttributeValueMapper.delete(pmsProductAttributeValueLambdaQueryWrapper);
        relateAndInsertList(productAttributeValueMapper, productParam.getProductAttributeValueList(), id);
        //关联专题
        LambdaQueryWrapper<CmsSubjectProductRelation> cmsSubjectProductRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cmsSubjectProductRelationLambdaQueryWrapper.eq(CmsSubjectProductRelation::getProductId, id);
        subjectProductRelationMapper.delete(cmsSubjectProductRelationLambdaQueryWrapper);
        relateAndInsertList(subjectProductRelationMapper, productParam.getSubjectProductRelationList(), id);
        //关联优选
        LambdaQueryWrapper<CmsPrefrenceAreaProductRelation> cmsPrefrenceAreaProductRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cmsPrefrenceAreaProductRelationLambdaQueryWrapper.eq(CmsPrefrenceAreaProductRelation::getProductId, id);
        prefrenceAreaProductRelationMapper.delete(cmsPrefrenceAreaProductRelationLambdaQueryWrapper);
        relateAndInsertList(prefrenceAreaProductRelationMapper, productParam.getPrefrenceAreaProductRelationList(), id);
        count = 1;
        return count;
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParam productParam) {
        //当前的sku信息
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        //当前没有sku直接删除
        if (CollUtil.isEmpty(currSkuList)) {
            LambdaQueryWrapper<PmsSkuStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PmsSkuStock::getProductId, id);
            skuStockMapper.delete(lambdaQueryWrapper);
            return;
        }

        //获取初始sku信息
        LambdaQueryWrapper<PmsSkuStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsSkuStock::getProductId, id);
        List<PmsSkuStock> oriStuList = skuStockMapper.selectList(lambdaQueryWrapper);
        //获取新增sku信息
        List<PmsSkuStock> insertSkuList = currSkuList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        //获取需要更新的sku信息
        List<PmsSkuStock> updateSkuList = currSkuList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        //获取需要删除的sku信息
        List<PmsSkuStock> removeSkuList = oriStuList.stream().filter(item -> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList, id);
        handleSkuStockCode(updateSkuList, id);
        //新增sku
        if (CollUtil.isNotEmpty(insertSkuList)) {
            relateAndInsertList(skuStockDao, insertSkuList, id);
        }
        //删除sku
        if (CollUtil.isNotEmpty(removeSkuList)) {
            List<Long> removeSkuIds = removeSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
            LambdaQueryWrapper<PmsSkuStock> removeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            removeLambdaQueryWrapper.in(PmsSkuStock::getId, removeSkuIds);
            skuStockMapper.delete(removeLambdaQueryWrapper);
        }
        //修改sku
        if (CollUtil.isNotEmpty(updateSkuList)) {
            for (PmsSkuStock pmsSkuStock : updateSkuList) {
                skuStockMapper.updateById(pmsSkuStock);
            }
        }

    }

    /**
     * 分页查询商品
     *
     * @param productQueryParam
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        Page<PmsProduct> page1 = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (productQueryParam.getPublishStatus() != null) {
            lambdaQueryWrapper.eq(PmsProduct::getPublishStatus, productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            lambdaQueryWrapper.eq(PmsProduct::getVerifyStatus, productQueryParam.getVerifyStatus());
        }
        if (!StringUtils.isEmpty(productQueryParam.getKeyword())) {
            lambdaQueryWrapper.like(PmsProduct::getKeywords, productQueryParam.getKeyword());
        }
        if (!StringUtils.isEmpty(productQueryParam.getProductSn())) {
            lambdaQueryWrapper.eq(PmsProduct::getProductSn, productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            lambdaQueryWrapper.eq(PmsProduct::getBrandId, productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            lambdaQueryWrapper.eq(PmsProduct::getProductCategoryId, productQueryParam.getProductCategoryId());
        }
        Page<PmsProduct> page = productMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(page);

    }

    /**
     * 批量修改审核状态
     *
     * @param ids          产品id
     * @param verifyStatus 审核状态
     * @param detail       审核详情
     */
    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProduct::getId, ids);
        int count = productMapper.update(product, lambdaQueryWrapper);
        List<PmsProductVerifyRecord> list = new ArrayList<>();
        //修改完审核状态后插入审核记录
        for (Long id : ids) {
            PmsProductVerifyRecord record = new PmsProductVerifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVertifyMan("test");
            list.add(record);
        }
        pmsProductVerifyRecordMapper.insert(list);
        return count;
    }

    /**
     * 批量修改商品上架状态
     *
     * @param ids
     * @param publishStatus
     */
    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProduct::getId, ids);
        return productMapper.update(record, lambdaQueryWrapper);
    }

    /**
     * 批量修改商品推荐状态
     *
     * @param ids
     * @param recommendStatus
     */
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProduct::getId, ids);
        return productMapper.update(record, lambdaQueryWrapper);
    }

    /**
     * 批量修改新品状态
     *
     * @param ids
     * @param newStatus
     */
    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProduct::getId, ids);
        return productMapper.update(record, lambdaQueryWrapper);

    }

    /**
     * 批量删除商品(假性删除,修改DeleteStatus状态实现)
     *
     * @param ids
     * @param deleteStatus
     */
    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {

        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PmsProduct::getId, ids);
        return productMapper.update(record, lambdaQueryWrapper);
    }

    /**
     * 根据商品名称或者货号模糊查询
     *
     * @param keyword
     */
    @Override
    public List<PmsProduct> list(String keyword) {
        LambdaQueryWrapper<PmsProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProduct::getDeleteStatus, 0);//未被删除
        if(StringUtils.hasText(keyword)){
            lambdaQueryWrapper.like(PmsProduct::getName,keyword);
            lambdaQueryWrapper.or().eq(PmsProduct::getDeleteStatus, 0).like(PmsProduct::getProductSn,keyword);
        }
        return productMapper.selectList(lambdaQueryWrapper);
    }

    private void relateAndInsertList(BaseMapper baseMapper, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            baseMapper.insert(dataList);
        } catch (Exception e) {
            log.error("创建产品出错：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            log.error("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
