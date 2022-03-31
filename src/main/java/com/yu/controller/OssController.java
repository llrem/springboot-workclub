package com.yu.controller;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.OSSClientBuilder;
import com.yu.common.api.Result;
import com.yu.dto.OssCallbackResult;
import com.yu.dto.OssPolicyResult;
import com.yu.entity.TmTaskFile;
import com.yu.service.OssService;
import com.yu.service.TmTaskFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Oss对象存储管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@RequestMapping("/aliyun/oss")
public class OssController {

    @Autowired
    private OssService ossService;
    @Autowired
    TmTaskFileService taskFileService;

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public Result<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return Result.success(result);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public Result<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return Result.success(ossCallbackResult);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> download(HttpServletResponse response,
                                   @RequestParam(value = "id") Long id) throws IOException {
        TmTaskFile taskFile = taskFileService.getById(id);
        if(taskFile != null){
            String objectName = taskFile.getPath()+'/'+taskFile.getName();
            boolean isDownload = ossService.download(objectName,response.getOutputStream());
            if (isDownload){
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(taskFile.getName(), "UTF-8"));
                return Result.success("success");
            }
        }
        return Result.failed();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> delete(@RequestParam(value = "id") Long id){
        TmTaskFile taskFile = taskFileService.getById(id);
        if(taskFile != null){
            String objectName = taskFile.getPath()+'/'+taskFile.getName();
            ossService.delete(objectName);
            taskFileService.removeById(id);
            return Result.success("success");
        }
        return Result.failed();
    }
}
