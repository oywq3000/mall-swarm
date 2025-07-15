package com.oyproj.admin.properties;

import com.aliyun.oss.model.LiveChannelListing;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("redis")
@Data
public class RedisProperties {
    private String database;
    private Key key;
    private Expire expire;

    @Data
    public static class Key{
        private String admin;
    }
    @Data
    public static class Expire{
        private Integer common;
    }
}
