package com.oyproj.gateway.config;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author oy
 * @description 网关白名单配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;
}
