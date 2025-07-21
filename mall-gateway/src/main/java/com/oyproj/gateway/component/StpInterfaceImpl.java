package com.oyproj.gateway.component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.oyproj.common.constant.AuthConstant;
import com.oyproj.common.dto.UserDto;

import java.util.List;

/**
 * @author oy
 * @description 自定义权限验证接口扩展
 */
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //返回此loginId拥有的权限码列表
        if(StpUtil.getLoginType().equals(loginType)){
            //后台用户需要返回
            UserDto userDto = (UserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
            return userDto.getPermissionList();
        }else{
            //前台用户则无需返回
            return null;
        }
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色码列表
        return null;
    }
}
