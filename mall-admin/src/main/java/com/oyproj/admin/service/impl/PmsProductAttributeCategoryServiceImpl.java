package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.PmsProductAttributeCategoryDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.PmsProductAttributeCategoryItem;
import com.oyproj.admin.mapper.PmsProductAttributeCategoryMapper;
import com.oyproj.admin.model.PmsProductAttributeCategory;
import com.oyproj.admin.service.PmsProductAttributeCategoryService;
import com.oyproj.common.api.IPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    private final PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    private final PmsProductAttributeCategoryDao productAttributeCategoryDao;

    /**
     * 创建属性分类
     *
     * @param name
     */
    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return productAttributeCategoryMapper.insert(productAttributeCategory);
    }

    /**
     * 修改属性分类
     *
     * @param id
     * @param name
     */
    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return productAttributeCategoryMapper.updateById(productAttributeCategory);
    }

    /**
     * 删除属性分类
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return productAttributeCategoryMapper.deleteById(id);
    }

    /**
     * 获取属性分类详情
     *
     * @param id
     */
    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryMapper.selectById(id);
    }

    /**
     * 分页查询属性分类
     *
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        Page<PmsProductAttributeCategory> page1 = new Page<>(pageNum,pageSize);
        Page<PmsProductAttributeCategory> page = productAttributeCategoryMapper.selectPage(page1, new LambdaQueryWrapper<>());
        return PageInfo.build(page);
    }

    /**
     * 获取包含属性的属性分类
     */
    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }
}
