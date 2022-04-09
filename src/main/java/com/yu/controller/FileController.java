package com.yu.controller;

import com.aliyun.oss.OSSClient;
import com.yu.common.api.Result;
import com.yu.dto.FileListParam;
import com.yu.dto.FileParam;
import com.yu.dto.PermissionParam;
import com.yu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

@RestController
@RequestMapping("task/file")
public class FileController {
    @Autowired
    OssService ossService;

    @GetMapping("/get_files")
    public Result<FileListParam> getFiles(@RequestParam(value = "path") String path){
       FileListParam list = ossService.listObjects(path);
       return Result.success(list);
    }

    @GetMapping("/download_file")
    public Result<String> downloadFile(HttpServletResponse response,
                                              @RequestParam(value = "path") String path) throws IOException {
        String objectName = "workclub/file/project/" + path;
        String[] pathItem = Pattern.compile("/").split(path);
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(pathItem[pathItem.length-1], "UTF-8"));
        ossService.download(objectName,response.getOutputStream());
        return Result.success("success");
    }

    @GetMapping("/delete_file")
    public Result<String> deleteFile(@RequestParam(value = "path") String path) {
        String objectName = "workclub/file/project/" + path;
        ossService.delete(objectName);
        return Result.success("success");
    }

    @GetMapping("/add_folder")
    public Result<String> addFolder(@RequestParam(value = "objectName") String objectName) {
        objectName = "workclub/file/project/" + objectName;
        ossService.addFolder(objectName);
        return Result.success("success");
    }
}
