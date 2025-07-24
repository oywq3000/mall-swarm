package com.oyproj.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.dao.UmsAdminRoleRelationDao;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.UmsAdminParam;
import com.oyproj.admin.dto.UpdateAdminPasswordParam;
import com.oyproj.mall.mapper.UmsAdminLoginLogMapper;
import com.oyproj.mall.mapper.UmsAdminMapper;
import com.oyproj.mall.mapper.UmsAdminRoleRelationMapper;
import com.oyproj.admin.service.UmsAdminCacheService;
import com.oyproj.admin.service.UmsAdminService;
import com.oyproj.common.api.IPageInfo;
import com.oyproj.common.constant.AuthConstant;
import com.oyproj.common.dto.UserDto;
import com.oyproj.common.exception.Asserts;
import com.oyproj.mall.model.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UmsAdminServiceImpl implements UmsAdminService {

    private final UmsAdminMapper adminMapper;

    private final UmsAdminRoleRelationMapper adminRoleRelationMapper;

    private final UmsAdminRoleRelationDao adminRoleRelationDao;

    private final UmsAdminLoginLogMapper adminLoginLogMapper;

    public final UmsAdminCacheService adminCacheService;


    /**
     * 根据用户名获取后台管理员
     *
     * @param username
     */
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        LambdaQueryWrapper<UmsAdmin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsAdmin::getUsername,username);
        return adminMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 注册功能
     *
     * @param umsAdminParam
     */
    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam,umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户

        List<UmsAdmin> umsAdmins = adminMapper.selectList(new LambdaQueryWrapper<UmsAdmin>().eq(UmsAdmin::getUsername, umsAdmin.getUsername()));
        if(umsAdmins.size()>0){
            //用户同名
            return null;
        }
        //密码加密
        String encodePassword = BCrypt.hashpw(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public SaTokenInfo login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        UmsAdmin admin = getAdminByUsername(username);
        if(admin==null){
            Asserts.fail("找不到该用户！");
        }
        if (!BCrypt.checkpw(password, admin.getPassword())) {
            Asserts.fail("密码不正确！");
        }
        if(admin.getStatus()!=1){
            Asserts.fail("该账号已被禁用！");
        }
        //实现登录功能
        StpUtil.login(admin.getId());
        UserDto userDto = new UserDto();
        userDto.setId(admin.getId());
        userDto.setUsername(admin.getUsername());
        userDto.setClientId(AuthConstant.ADMIN_CLIENT_ID);
        List<UmsResource> resourceList = getResourceList(admin.getId());
        List<String> permissionList = resourceList.stream().map(res->res.getId()+":"+res.getName()).toList();
        userDto.setPermissionList(permissionList);
        //将用户信息存储到Session中
        StpUtil.getSession().set(AuthConstant.STP_ADMIN_INFO,userDto);
        //获取当前登入用户Token信息
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        insertLoginLog(admin);
        return saTokenInfo;
    }

    /**
     * 根据用户id获取用户
     *
     * @param id
     */
    @Override
    public UmsAdmin getItem(Long id) {
        return adminMapper.selectById(id);
    }

    /**
     * 根据用户名或昵称分页查询用户
     *
     * @param keyword
     * @param pageSize
     * @param pageNum
     */
    @Override
    public IPageInfo<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsAdmin> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<UmsAdmin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(keyword)){
            lambdaQueryWrapper.and(wrapper->wrapper
                    .like(UmsAdmin::getUsername,keyword)
                    .or()
                    .like(UmsAdmin::getNickName,keyword));
        }
        Page<UmsAdmin> page = adminMapper.selectPage(page1, lambdaQueryWrapper);
        PageInfo<UmsAdmin> pageInfo = PageInfo.build(page);

        return pageInfo;
    }

    /**
     * 修改指定用户信息
     *
     * @param id
     * @param admin
     */
    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = adminMapper.selectById(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码不同无需修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(BCrypt.hashpw(admin.getPassword()));
            }
        }
        int count = adminMapper.updateById(admin);
        //删除原先的cache
        adminCacheService.delAdmin(id);
        return count;
    }

    /**
     * 删除指定用户
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        int count = adminMapper.deleteById(id);
        adminCacheService.delAdmin(id);
        return count;
    }

    /**
     * 修改用户角色关系
     *
     * @param adminId
     * @param roleIds
     */
    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds==null?0:roleIds.size();
        //先删除原来的关系
        LambdaQueryWrapper<UmsAdminRoleRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsAdminRoleRelation::getAdminId,adminId);
        adminRoleRelationMapper.delete(lambdaQueryWrapper);
        //建立新关系
        if(!CollectionUtils.isEmpty(roleIds)){
            List<UmsAdminRoleRelation> adminRoleRelations = new ArrayList<>();
            for(Long roleId:roleIds){
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                adminRoleRelations.add(roleRelation);
            }
            adminRoleRelationMapper.insert(adminRoleRelations);
        }
        return count;
    }

    /**
     * 获取用户对于角色
     *
     * @param adminId
     */
    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    /**
     * 获取指定用户的可访问资源
     *
     * @param adminId
     */
    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return adminRoleRelationDao.getResourceList(adminId);
    }

    /**
     * 修改密码
     *
     * @param param
     */
    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isNotEmpty(param.getUsername())
        ||StrUtil.isEmpty(param.getOldPassword())
        ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        LambdaQueryWrapper<UmsAdmin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsAdmin::getUsername,param.getUsername());
        UmsAdmin umsAdmin = adminMapper.selectOne(lambdaQueryWrapper);
        if(umsAdmin==null){
            return -2;
        }
        if(!BCrypt.checkpw(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(BCrypt.hashpw(param.getNewPassword()));
        adminMapper.updateById(umsAdmin);
        adminCacheService.delAdmin(umsAdmin.getId());
        return 1;
    }

    /**
     * 获取当前登录后台用户
     */
    @Override
    public UmsAdmin getCurrentAdmin() {
        UserDto userDto = (UserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
        UmsAdmin admin = adminCacheService.getAdmin(userDto.getId());
        if(admin==null){
            UmsAdmin umsAdmin = adminMapper.selectById(userDto.getId());
            adminCacheService.setAdmin(umsAdmin);
        }
        return admin;
    }

    /**
     * 登出操作
     */
    @Override
    public void logout() {
        //先清空缓存
        UserDto userDto = (UserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
        adminCacheService.delAdmin(userDto.getId());
        //再调用sa-token的登出方法
        StpUtil.logout();
    }


    /**
     * 添加登入记录
     */
    private void insertLoginLog(UmsAdmin admin){
        if(admin==null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        adminLoginLogMapper.insert(loginLog);
    }
}
