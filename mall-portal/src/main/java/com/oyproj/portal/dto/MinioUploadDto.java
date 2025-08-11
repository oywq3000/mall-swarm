package com.oyproj.portal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author oy
 * @description 文件上传返回结果
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinioUploadDto {
    @Schema(title = "文件访问URL")
    private String url;
    @Schema(title = "文件名称")
    private String name;
}
