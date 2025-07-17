package com.oyproj.admin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性类
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssProperties {
    /**
     * OSS 对外服务的访问域名
     */
    private String endpoint;
    /**
     * 访问身份验证中用到用户标识
     */
    private String accessKeyId;
    /**
     * 用户用于加密签名字符串和 OSS 用来验证签名字符串的密钥
     */
    private String accessKeySecret;
    /**
     * OSS 的存储空间
     */
    private String bucketName;
    /**
     * 签名相关配置
     */
    private Policy policy;
    /**
     * 上传文件最大大小，单位 M
     */
    private int maxSize;
    /**
     * 文件上传成功后的回调地址
     */
    private String callback;
    /**
     * 上传文件夹路径前缀
     */
    private Dir dir;

    // 内部类，用于存储 policy 配置
    public static class Policy {
        /**
         * 签名有效期，单位秒
         */
        private int expire;

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }
    }

    // 内部类，用于存储 dir 配置
    public static class Dir {
        /**
         * 上传文件夹路径前缀
         */
        private String prefix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    // Getter 和 Setter 方法
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}