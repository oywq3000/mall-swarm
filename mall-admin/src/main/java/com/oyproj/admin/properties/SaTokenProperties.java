package com.oyproj.admin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于绑定 sa-token 配置的属性类
 */
@Component
@ConfigurationProperties(prefix = "springdoc.sa-token")
public class SaTokenProperties {
    /**
     * token名称 (同时也是cookie名称)
     */
    private String tokenName;
    /**
     * token有效期，单位秒，-1代表永不过期
     */
    private Long timeout;
    /**
     * token临时有效期 (指定时间内无操作就视为token过期)，单位秒
     */
    private Long activeTimeout;
    /**
     * 是否允许同一账号并发登录 (为false时新登录挤掉旧登录)
     */
    private Boolean concurrent;
    /**
     * 在多人登录同一账号时，是否共用一个token (为false时每次登录新建一个token)
     */
    private Boolean share;
    /**
     * token风格
     */
    private String tokenStyle;
    /**
     * 是否输出操作日志
     */
    private Boolean log;
    /**
     * 是否从cookie中读取token
     */
    private Boolean readCookie;
    /**
     * 是否从head中读取token
     */
    private Boolean readHeader;
    /**
     * token前缀
     */
    private String tokenPrefix;
    /**
     * jwt秘钥
     */
    private String jwtSecretKey;
    /**
     * 是否打印banner
     */
    private Boolean print;

    // Getters 和 Setters
    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getActiveTimeout() {
        return activeTimeout;
    }

    public void setActiveTimeout(Long activeTimeout) {
        this.activeTimeout = activeTimeout;
    }

    public Boolean getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Boolean concurrent) {
        this.concurrent = concurrent;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getTokenStyle() {
        return tokenStyle;
    }

    public void setTokenStyle(String tokenStyle) {
        this.tokenStyle = tokenStyle;
    }

    public Boolean getLog() {
        return log;
    }

    public void setLog(Boolean log) {
        this.log = log;
    }

    public Boolean getReadCookie() {
        return readCookie;
    }

    public void setReadCookie(Boolean readCookie) {
        this.readCookie = readCookie;
    }

    public Boolean getReadHeader() {
        return readHeader;
    }

    public void setReadHeader(Boolean readHeader) {
        this.readHeader = readHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public Boolean getPrint() {
        return print;
    }

    public void setPrint(Boolean print) {
        this.print = print;
    }
}