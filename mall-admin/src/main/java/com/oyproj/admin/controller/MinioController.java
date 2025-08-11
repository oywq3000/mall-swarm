package com.oyproj.admin.controller;

import com.oyproj.admin.dto.MinioUploadDto;
import com.oyproj.admin.service.MinioService;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author oy
 * @description MinIO对象存储管理
 */

@RestController
@Tag(name = "MinioController", description = "MinIO对象存储管理")
@RequestMapping("/minio")
@Slf4j
@RequiredArgsConstructor
public class MinioController {

    private final MinioService miniService;

    @Operation(summary = "文件上传")
    @PostMapping(value = "/upload")
    public CommonResult upload(@RequestPart("file") MultipartFile file) {
        MinioUploadDto minioUploadDto = miniService.upload(file);
        if (minioUploadDto != null) return CommonResult.success(minioUploadDto);
        //上传失败
        return CommonResult.failed();
    }

    @Operation(summary = "文件删除")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("objectName") String objectName) {

        boolean isUpdated =  miniService.delete(objectName);
        if(isUpdated) return CommonResult.success(null);
        return CommonResult.failed();
    }

}
