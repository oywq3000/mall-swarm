package com.oyproj.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.oyproj.admin.dto.OssCallbackParam;
import com.oyproj.admin.dto.OssCallbackResult;
import com.oyproj.admin.dto.OssPolicyResult;
import com.oyproj.admin.properties.AliyunOssProperties;
import com.oyproj.admin.service.OssService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {
    private final OSSClient ossClient;
    private final AliyunOssProperties ossProperties;
    /**
     * oss上传策略生成
     */
    @Override
    public OssPolicyResult policy() {
        OssPolicyResult result = new OssPolicyResult();
        //存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = ossProperties.getDir().getPrefix()+sdf.format(new Date());
        //签名有效期
        long expireEndTime = System.currentTimeMillis()+ossProperties.getPolicy().getExpire()*1000L;
        Date expiration = new Date(expireEndTime);
        //文件大小
        long maxSize = ossProperties.getMaxSize()*1024L*1024L;
        //回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(ossProperties.getCallback());
        callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");
        //提交节点
        String action = "http://" +ossProperties.getBucketName()+"."+ossProperties.getEndpoint();
        try{
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE,0,maxSize);
            policyConds.addConditionItem(MatchMode.StartWith,PolicyConditions.COND_KEY,dir);
            String postPolicy = ossClient.generatePostPolicy(expiration,policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(policy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes(StandardCharsets.UTF_8));
            //返回结果
            result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(action);
        }catch (Exception e){
            log.error("签名生成失败",e);
        }
        return result;
    }

    /**
     * oss上传成功回调
     *
     * @param request
     */
    @Override
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult result= new OssCallbackResult();
        String filename = request.getParameter("filename");
        filename = "http://".concat(ossProperties.getBucketName()).concat(".").concat(ossProperties.getEndpoint()).concat("/").concat(filename);
        result.setFilename(filename);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));
        result.setWidth(request.getParameter("width"));
        result.setHeight(request.getParameter("height"));
        return result;
    }
}
