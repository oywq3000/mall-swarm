package com.oyproj.portal.service.impl;
import com.oyproj.portal.dto.BucketPolicyConfigDto;
import com.oyproj.portal.properties.MinioProperties;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.oyproj.portal.dto.MinioUploadDto;
import com.oyproj.portal.service.MinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioProperties minioProperties;

    @Override
    public MinioUploadDto upload(MultipartFile file) {
        try {
            //创建一个Minio的java客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                    .build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if(isExist){
                log.info("存储桶已经存在！");
            }else{
                //创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                BucketPolicyConfigDto bucketPolicyConfigDto = createBucketPolicyConfigDto(minioProperties.getBucketName());
                SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                        .build();
                minioClient.setBucketPolicy(setBucketPolicyArgs);
            }
            String filename = file.getOriginalFilename();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 设置存储对象名称
            String objectName = sdf.format(new Date()) + "/" + filename;
            // 使用putObject上传一个文件到存储桶中
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
            minioClient.putObject(putObjectArgs);
            log.info("文件上传成功!");
            MinioUploadDto minioUploadDto = new MinioUploadDto();
            minioUploadDto.setName(filename);
            minioUploadDto.setUrl(minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + objectName);

            return minioUploadDto;

        }catch (Exception e){
            e.printStackTrace();
            log.info("上传发生错误: {}！", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(String objectName) {
        try{
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                    .build();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioProperties.getBucketName())
                    .object(objectName).build());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除【{}】失败",objectName);
        }
        return false;
    }

    private BucketPolicyConfigDto createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::"+bucketName+"/*").build();
        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement))
                .build();
    }
}
