package com.oyproj.portal.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.common.constant.AuthConstant;
import com.oyproj.common.dto.UserDto;
import com.oyproj.common.exception.Asserts;
import com.oyproj.mall.mapper.UmsMemberLevelMapper;
import com.oyproj.mall.mapper.UmsMemberMapper;
import com.oyproj.mall.model.UmsMember;
import com.oyproj.mall.model.UmsMemberLevel;
import com.oyproj.portal.properties.RedisProperties;
import com.oyproj.portal.service.UmsMemberCacheService;
import com.oyproj.portal.service.UmsMemberService;
import com.oyproj.portal.util.StpMemberUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * @author oy
 * @description 会员管理Service实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UmsMemberServiceImpl implements UmsMemberService {

    private final UmsMemberMapper memberMapper;
    private final UmsMemberLevelMapper memberLevelMapper;
    private final UmsMemberCacheService memberCacheService;
    private final RedisProperties redisProperties;

    /**
     * 根据用户名获取会员
     *
     * @param username
     */
    @Override
    public UmsMember getByUsername(String username) {
        LambdaQueryWrapper<UmsMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMember::getUsername, username);
        return memberMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 根据会员编号获取会员
     *
     * @param id
     */
    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectById(id);
    }

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param telephone
     * @param authCode
     */
    @Override
    public void register(String username, String password, String telephone, String authCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误");
        }
        //查询是否已有该用户
        LambdaQueryWrapper<UmsMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMember::getUsername,username);
        lambdaQueryWrapper.or().eq(UmsMember::getPhone,telephone);
        UmsMember umsMember = memberMapper.selectOne(lambdaQueryWrapper);
        if(umsMember!=null){
            Asserts.fail("该用户已经存在");
        }
        //没有改用户进行添加操作
        UmsMember newMember = new UmsMember();
        newMember.setUsername(username);
        newMember.setPhone(telephone);
        newMember.setPassword(BCrypt.hashpw(password));
        newMember.setCreateTime(new Date());
        newMember.setStatus(1);//帐号启用状态:0->禁用；1->启用
        //获取默认会员等级并设置
        LambdaQueryWrapper<UmsMemberLevel> memberLevelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLevelLambdaQueryWrapper.eq(UmsMemberLevel::getDefaultStatus,1);//是否为默认等级：0->不是；1->是
        List<UmsMemberLevel> umsMemberLevels = memberLevelMapper.selectList(memberLevelLambdaQueryWrapper);
        if(!CollectionUtils.isEmpty(umsMemberLevels)){
            newMember.setMemberLevelId(umsMemberLevels.get(0).getId());
        }
        memberMapper.insert(newMember);
        newMember.setPassword(null); //清空密码
    }

    /**
     * 生成验证码
     *
     * @param telephone
     */
    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        memberCacheService.setAuthCode(telephone,sb.toString());
        return sb.toString();
    }

    /**
     * 修改密码
     *
     * @param telephone
     * @param password
     * @param authCode
     */
    @Override
    public void updatePassword(String telephone, String password, String authCode) {
        LambdaQueryWrapper<UmsMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMember::getPhone,telephone);
        UmsMember umsMember = memberMapper.selectOne(lambdaQueryWrapper);
        if(umsMember==null){
            Asserts.fail("该账号不存在");
        }
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误");
        }
        umsMember.setPassword(BCrypt.hashpw(password));
        memberMapper.updateById(umsMember);
        memberCacheService.delMember(umsMember.getId());//清理原始的缓存
    }


    /**
     * 获取当前登录会员
     */
    @Override
    public UmsMember getCurrentMember() {
        UserDto userDto = (UserDto) StpMemberUtil.getSession().get(AuthConstant.STP_MEMBER_INFO);
        UmsMember member = memberCacheService.getMember(userDto.getId());
        if(member==null){
            member = getById(userDto.getId());
            memberCacheService.setMember(member);
        }
        return member;
    }

    /**
     * 根据会员id修改会员积分
     *
     * @param id
     * @param integration
     */
    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record=new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateById(record);
        memberCacheService.delMember(id);
    }

    /**
     * 登录后获取token
     *
     * @param username
     * @param password
     */
    @Override
    public SaTokenInfo login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        UmsMember member = getByUsername(username);
        if(member==null){
            Asserts.fail("找不到该用户！");
        }
        if (!BCrypt.checkpw(password, member.getPassword())) {
            Asserts.fail("密码不正确！");
        }
        if(member.getStatus()!=1){
            Asserts.fail("该账号已被禁用！");
        }
        //登入校验
        StpMemberUtil.login(member.getId());
        UserDto userDto = new UserDto();
        userDto.setId(member.getId());
        userDto.setUsername(member.getUsername());
        userDto.setClientId(AuthConstant.PORTAL_CLIENT_ID);
        // 将用户信息存储到Session中
        StpMemberUtil.getSession().set(AuthConstant.STP_MEMBER_INFO,userDto);
        //获取当前登录用户Token信息
        return StpMemberUtil.getTokenInfo();
    }

    /**
     * 登出功能
     */
    @Override
    public void logout() {
        //先清空缓存
        UserDto userDto = (UserDto) StpMemberUtil.getSession().get(AuthConstant.STP_MEMBER_INFO);
        memberCacheService.delMember(userDto.getId());
        //再调用sa-token的登出方法
        StpMemberUtil.logout();
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode,String telephone){
        if(StringUtils.isNotBlank(authCode)){
            String realAuthCode = memberCacheService.getAuthCode(telephone);
            return authCode.equals(realAuthCode);
        }
        return false;
    }
}
