package com.oyproj.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.admin.mapper.UmsResourceCategoryMapper;
import com.oyproj.admin.model.UmsResourceCategory;
import com.oyproj.admin.service.UmsResourceCategoryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UmsResourceCategoryServiceImpl extends ServiceImpl<UmsResourceCategoryMapper,UmsResourceCategory> implements UmsResourceCategoryService {
    /**
     * 获取所有资源分类
     */
    @Override
    public List<UmsResourceCategory> listAll() {
        return list();
    }

    /**
     * 创建资源分类
     *
     * @param umsResourceCategory
     */
    @Override
    public int create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        boolean isSave = save(umsResourceCategory);
        if(isSave){
            return 1;
        }
        return 0;
    }

    /**
     * 修改资源分类
     *
     * @param id
     * @param umsResourceCategory
     */
    @Override
    public int update(Long id, UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        boolean isUpdated = updateById(umsResourceCategory);
        if(isUpdated){
            return 1;
        }
        return 0;
    }

    /**
     * 删除资源分类
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        boolean isRemoved = removeById(id);
        if(isRemoved) return 1;
        return 0;
    }
}
