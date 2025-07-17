package com.oyproj.admin.config;

import com.aliyun.oss.OSSClient;
import com.oyproj.admin.properties.AliyunOssProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author oy
 * @description OSS对象存储相关配置
 */
@Configuration
@RequiredArgsConstructor
public class OssConfig {
    private final AliyunOssProperties aliyunOssProperties;
    @Bean
    public OSSClient ossClient(){
        return new OSSClient(aliyunOssProperties.getEndpoint(),aliyunOssProperties.getAccessKeyId(),aliyunOssProperties.getAccessKeySecret());
    }
}
