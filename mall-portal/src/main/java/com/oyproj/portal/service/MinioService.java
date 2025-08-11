package com.oyproj.portal.service;


import com.oyproj.portal.dto.MinioUploadDto;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    MinioUploadDto upload(MultipartFile file);

    boolean delete(String objectName);
}
