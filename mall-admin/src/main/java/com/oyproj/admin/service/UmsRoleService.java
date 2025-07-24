package com.oyproj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyproj.mall.model.UmsMenu;
import com.oyproj.mall.model.UmsResource;
import com.oyproj.mall.model.UmsRole;
import com.oyproj.common.api.PageInfo;

import java.util.List;

/**
 * @author oy
 * @description 后台角色管理Service
 */
public interface UmsRoleService extends IService<UmsRole> {
    int create(UmsRole role);

    int update(Long id, UmsRole role);

    int delete(List<Long> ids);

    PageInfo<UmsRole> listRole(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long adminId);

    List<UmsMenu> listMenu(Long roleId);

    List<UmsResource> listResource(Long roleId);

    int allocMenu(Long roleId, List<Long> menuIds);

    int allocResource(Long roleId, List<Long> resourceIds);
}
