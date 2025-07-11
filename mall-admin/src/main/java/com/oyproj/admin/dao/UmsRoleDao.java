package com.oyproj.admin.dao;

import com.oyproj.admin.model.UmsMenu;
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

    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
}
