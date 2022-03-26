package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.common.api.Result;
import com.yu.dto.MemberParam;
import com.yu.entity.PmProject;
import com.yu.entity.PmProjectUser;
import com.yu.service.PmProjectService;
import com.yu.service.PmProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
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

//    @Value("${file.upload.url}")
//    private String uploadFilePath;
    @Autowired
    PmProjectService projectService;
    @Autowired
    PmProjectUserService projectUserService;

    @GetMapping("/get_projects")
    public Result<List<PmProject>> getProjects(@RequestParam(value = "id") Long id){
        List<PmProject> projectList = projectUserService.getProjectsByUserId(id);
        return Result.success(projectList);
    }

    @GetMapping("/search_project")
    public Result<List<PmProject>> searchProjects(@RequestParam(value = "key")String key){
        List<PmProject> projectList = projectService.searchProjects(key);
        return Result.success(projectList);
    }

    @PostMapping("/create_project")
    public Result<PmProject> createProject(@RequestBody PmProject project){
        project.setCreateDate(LocalDateTime.now());
        project.setStatus(1);
        if(project.getPicture().equals("")){
            project.setPicture("http://workclub-oss.oss-cn-chengdu.aliyuncs.com/workclub/project_cover/default.png");
        }
        boolean isSave = projectService.save(project);

        PmProjectUser projectUser = new PmProjectUser();
        projectUser.setUserId(project.getCreateUserId());
        projectUser.setProjectId(project.getId());
        boolean isSave2 = projectUserService.save(projectUser);

        if (isSave && isSave2){
            return Result.success(project);
        }
        return Result.failed();
    }

    @GetMapping("/get_project_members")
    public Result<List<MemberParam>> getProjectMembers(@RequestParam(value = "projectId") Long id){
        List<MemberParam> memberList = projectUserService.getMemberListByProjectId(id);
        return Result.success(memberList);
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
