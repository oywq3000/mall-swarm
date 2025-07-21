package com.oyproj.portal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.oyproj.portal.mapper","com.oyproj.portal.dao"})
public class MybatisConfig {
}
