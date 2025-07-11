package com.oyproj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyproj.admin.model.UmsMenu;
import com.oyproj.admin.model.UmsRole;
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

    List<UmsMenu> listMenu(Long roleId);
}
