package com.oyproj.admin.service;

import com.oyproj.admin.dto.MinioUploadDto;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    MinioUploadDto upload(MultipartFile file);

    boolean delete(String objectName);
}
