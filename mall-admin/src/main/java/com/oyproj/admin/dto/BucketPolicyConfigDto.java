package com.oyproj.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author oy
 * @description Minio Bucket 访问策略配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class BucketPolicyConfigDto {
    private String Version;
    private List<Statement> Statement;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Builder
    public static class Statement {
        private String Effect;
        private String Principal;
        private String Action;
        private String Resource;

    }
}
