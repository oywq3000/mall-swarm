package com.oyproj.admin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO 配置属性类
 */
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * MinIO 服务所在地址
     */
    private String endpoint;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 访问的 key
     */
    private String accessKey;

    /**
     * 访问的秘钥
     */
    private String secretKey;

    // Getter 和 Setter 方法
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}