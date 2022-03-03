package com.yu.controller;

import com.yu.common.api.Result;
import com.yu.entity.PmProject;
import com.yu.service.PmProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llrem
 * @since 2022-02-20
 */
@RestController
@RequestMapping("/project")
public class PmProjectController {

    @Value("${file.upload.url}")
    private String uploadFilePath;
    @Autowired
    private PmProjectService projectService;

    @GetMapping("/get_projects")
    public Result<List<PmProject>> getProjects(@RequestParam(value = "id") Long id){
        List<PmProject> projectList = projectService.getByCreateUserId(id);
        if(projectList.size()>0){
            return Result.success(projectList);
        }
        return Result.failed();
    }

    @GetMapping("/search_project")
    public Result<List<PmProject>> searchProjects(@RequestParam(value = "key")String key){
        List<PmProject> projectList = projectService.searchProjects(key);
        return Result.success(projectList);
    }

    @PostMapping("/create_project")
    public Result<PmProject> createProject(@RequestBody PmProject project){
        project.setCreateDate(new Date());
        project.setStatus(1);
        if(project.getPicture().equals("")){
            project.setPicture("http://workclub-oss.oss-cn-chengdu.aliyuncs.com/workclub/project_cover/default.png");
        }
        boolean isSave = projectService.save(project);
        if (isSave){
            return Result.success(project);
        }
        return Result.failed();
    }

}

//    将文件上传至本地文件夹
//    @PostMapping("/upload_cover")
//    public Result<PmProject> createProject(@RequestParam("file") MultipartFile file,
//                                           @RequestParam("username") String username){
//        String fileName = file.getOriginalFilename();  // 文件名
//        File dest = new File(uploadFilePath + "/projectCover/"+username+'/'+fileName);
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try {
//            file.transferTo(dest);
//        } catch (Exception e) {
//            return Result.failed("程序错误，请重新上传");
//        }
//        PmProject project = new PmProject();
//        project.setPicture(dest.getPath());
//        return Result.success(project);
//    }
