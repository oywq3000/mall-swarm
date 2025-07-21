package com.oyproj.portal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于绑定 application.yml 中 sa-token 配置的属性类
 */
@Component
@ConfigurationProperties(prefix = "sa-token")
public class SaTokenProperties {

    /**
     * token 名称 (同时也是 cookie 名称)
     */
    private String tokenName;

    /**
     * token 有效期，单位秒，-1 代表永不过期
     */
    private Long timeout;

    /**
     * token 临时有效期 (指定时间内无操作就视为 token 过期)，单位秒
     */
    private Long activeTimeout;

    /**
     * 是否允许同一账号并发登录 (为 false 时新登录挤掉旧登录)
     */
    private Boolean concurrent;

    /**
     * 在多人登录同一账号时，是否共用一个 token (为 false 时每次登录新建一个 token)
     */
    private Boolean share;

    /**
     * token 风格
     */
    private String tokenStyle;

    /**
     * 是否输出操作日志
     */
    private Boolean log;

    /**
     * 是否从 cookie 中读取 token
     */
    private Boolean readCookie;

    /**
     * 是否从 head 中读取 token
     */
    private Boolean readHeader;

    /**
     * token 前缀
     */
    private String tokenPrefix;

    /**
     * jwt 秘钥
     */
    private String jwtSecretKey;

    /**
     * 是否打印 banner
     */
    private Boolean print;

    // Getter 和 Setter 方法
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