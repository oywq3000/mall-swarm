package com.oyproj.mall.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PmsSearchHistory implements Serializable {
    private Long id;
    @Schema(title = "用户ID")
    private Long memberId;
    @Schema(title = "关键词")
    private String keyword;
    @Schema(title = "搜索时间")
    private Date createTime;
}
