package com.oyproj.admin.config;


import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author oy
 * @description Sa-Token相关配置
 */
@Configuration
public class SaTokenConfig {
    //Sa-Token整合jwt(Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt(){
        return new StpLogicJwtForSimple();
    }
}
