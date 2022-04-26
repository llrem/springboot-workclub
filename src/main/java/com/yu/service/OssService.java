package com.yu.service;

import com.aliyun.oss.model.OSSObjectSummary;
import com.yu.common.api.Result;
import com.yu.dto.FileListParam;
import com.yu.dto.FileParam;
import com.yu.dto.OssCallbackResult;
import com.yu.dto.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.util.List;

/**
 * Oss对象存储管理Service
 * Created by macro on 2018/5/17.
 */
public interface OssService {
    /**
     * Oss上传策略生成
     */
    OssPolicyResult policy();
    /**
     * Oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);

    boolean download(String objectName,OutputStream os);

    void delete(String objectName);

    void deleteFolder(String objectName);

    FileListParam listObjects(String projectId);

    boolean addFolder(String objectName);
}
