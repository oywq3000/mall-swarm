package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.admin.dao.UmsRoleDao;
import com.oyproj.admin.mapper.UmsMenuMapper;
import com.oyproj.admin.mapper.UmsRoleMapper;
import com.oyproj.admin.model.UmsMenu;
import com.oyproj.admin.model.UmsRole;
import com.oyproj.admin.service.UmsResourceService;
import com.oyproj.admin.service.UmsRoleService;
import com.oyproj.common.api.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author oy
 * @description 后台角色管理Service实现类
 */
@Service
@RequiredArgsConstructor
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper, UmsRole> implements UmsRoleService {


    private final UmsRoleDao umsRoleDao;
    private final UmsResourceService resourceService;
    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        boolean isSaved = save(role);
        if (isSaved) return 1;
        else return 0;
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        boolean isUpdated = updateById(role);
        if (isUpdated) return 1;
        else return 0;
    }

    @Override
    public int delete(List<Long> ids) {
        boolean isRemoved = removeBatchByIds(ids);
        resourceService.initPathResourceMap();
        if (isRemoved) return 1;
        else return 0;
    }

    @Override
    public PageInfo<UmsRole> listRole(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsRole> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<UmsRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            lambdaQueryWrapper.like(UmsRole::getName,keyword);
        }
        Page<UmsRole> page = baseMapper.selectPage(page1, lambdaQueryWrapper);

        return PageInfo.<UmsRole>builder()
                .current(page.getCurrent())
                .records(page.getRecords())
                .pages((int)page.getPages())
                .size((int) page.getSize())
                .total(page.getTotal())
                .build();
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
       return umsRoleDao.getMenuListByRoleId(roleId);
    }


    //todo list
}
