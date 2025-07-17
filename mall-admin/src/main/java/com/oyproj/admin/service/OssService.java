package com.oyproj.admin.service;

import com.oyproj.admin.dto.OssCallbackResult;
import com.oyproj.admin.dto.OssPolicyResult;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author oy
 * @description oss上传管理Service
 */
public interface OssService {
    /**
     * oss上传策略生成
     */
    OssPolicyResult policy();
    /**
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
}
