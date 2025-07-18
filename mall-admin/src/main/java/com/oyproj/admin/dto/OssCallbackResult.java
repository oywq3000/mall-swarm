package com.oyproj.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author oy
 * @description oss上传文件的返回结果
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OssCallbackResult {
    @Schema(title = "文件名称")
    private String filename;
    @Schema(title = "文件大小")
    private String size;
    @Schema(title = "文件的mimeType")
    private String mimeType;
    @Schema(title = "图片文件的宽")
    private String width;
    @Schema(title = "图片文件的高")
    private String height;
}
