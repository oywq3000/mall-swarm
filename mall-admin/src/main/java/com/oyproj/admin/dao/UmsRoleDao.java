package com.oyproj.admin.dao;

import com.oyproj.admin.model.UmsMenu;
import com.oyproj.admin.model.UmsResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author oy
 * @description 自定义后台角色管理Dao
 */

public interface UmsRoleDao {
    /**
     * 根据后台用户ID获取菜单
     */
    //todo
    /**
     * 根据角色ID获取菜单
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据角色ID获取资源
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);

    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);
}
