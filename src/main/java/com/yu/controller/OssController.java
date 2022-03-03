package com.yu.controller;


import com.yu.common.api.Result;
import com.yu.dto.OssCallbackResult;
import com.yu.dto.OssPolicyResult;
import com.yu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Oss对象存储管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@RequestMapping("/aliyun/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public Result<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return Result.success(result);
    }

    @RequestMapping(value = "callback", method = RequestMethod.POST)
    @ResponseBody
    public Result<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return Result.success(ossCallbackResult);
    }

}
