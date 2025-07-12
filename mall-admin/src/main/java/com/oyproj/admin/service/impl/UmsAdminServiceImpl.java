package com.oyproj.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.oyproj.admin.dao.UmsAdminRoleRelationDao;
import com.oyproj.admin.dto.UmsAdminParam;
import com.oyproj.admin.dto.UpdateAdminPasswordParam;
import com.oyproj.admin.mapper.UmsAdminMapper;
import com.oyproj.admin.mapper.UmsAdminRoleRelationMapper;
import com.oyproj.admin.model.UmsAdmin;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.admin.model.UmsRole;
import com.oyproj.admin.service.UmsAdminService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UmsAdminServiceImpl implements UmsAdminService {

    private final UmsAdminMapper adminMapper;

    private final UmsAdminRoleRelationMapper adminRoleRelationMapper;

    private final UmsAdminRoleRelationDao umsAdminRoleRelationDao;

    /**
     * 根据用户名获取后台管理员
     *
     * @param username
     */
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        return null;
    }

    /**
     * 注册功能
     *
     * @param umsAdminParam
     */
    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        return null;
    }

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public SaTokenInfo login(String username, String password) {
        return null;
    }

    /**
     * 根据用户id获取用户
     *
     * @param id
     */
    @Override
    public UmsAdmin getItem(Long id) {
        return null;
    }

    /**
     * 根据用户名或昵称分页查询用户
     *
     * @param keyword
     * @param pageSize
     * @param pageNum
     */
    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 修改指定用户信息
     *
     * @param id
     * @param admin
     */
    @Override
    public int update(Long id, UmsAdmin admin) {
        return 0;
    }

    /**
     * 删除指定用户
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return 0;
    }

    /**
     * 修改用户角色关系
     *
     * @param adminId
     * @param roleIds
     */
    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        return 0;
    }

    /**
     * 获取用户对于角色
     *
     * @param adminId
     */
    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return null;
    }

    /**
     * 获取指定用户的可访问资源
     *
     * @param adminId
     */
    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return null;
    }

    /**
     * 修改密码
     *
     * @param updatePasswordParam
     */
    @Override
    public int updatePassword(UpdateAdminPasswordParam updatePasswordParam) {
        return 0;
    }

    /**
     * 获取当前登录后台用户
     */
    @Override
    public UmsAdmin getCurrentAdmin() {
        return null;
    }

    /**
     * 登出操作
     */
    @Override
    public void logout() {

    }
}
