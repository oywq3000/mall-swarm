package com.oyproj.portal.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author oy
 * @description Redis 配置属性类，用于绑定 application.yml 中的 Redis 配置。
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String database;
    private final Keys keys = new Keys();
    private final Expire expire = new Expire();

    public static class Keys {
        private String authCode;
        private String orderId;
        private String member;

        // Getters 和 Setters
        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }
    }

    public static class Expire {
        private int authCode;
        private int common;

        // Getters 和 Setters
        public int getAuthCode() {
            return authCode;
        }

        public void setAuthCode(int authCode) {
            this.authCode = authCode;
        }

        public int getCommon() {
            return common;
        }

        public void setCommon(int common) {
            this.common = common;
        }
    }

    // Getters 和 Setters
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Keys getKeys() {
        return keys;
    }

    public Expire getExpire() {
        return expire;
    }
}
