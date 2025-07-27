package com.oyproj.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author oy
 * @description MyBatis相关配置
 */
@Configuration
@MapperScan({"com.oyproj.search.mapper","com.oyproj.search.dao"})
public class MybatisConfig {
}
