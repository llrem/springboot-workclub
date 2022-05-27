package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yu.common.api.Result;
import com.yu.dto.InvitationParam;
import com.yu.dto.MemberParam;
import com.yu.entity.*;
import com.yu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@EnableScheduling
@RequestMapping("/project")
public class ProjectController {

//    @Value("${file.upload.url}")
//    private String uploadFilePath;
    @Autowired
    PmProjectService projectService;
    @Autowired
    PmProjectUserService projectUserService;
    @Autowired
    PmUserInviteService userInviteService;
    @Autowired
    UmUserService userService;
    @Autowired
    UmUserRoleService userRoleService;
    @Autowired
    PmTaskUndoneService taskUndoneService;

    @GetMapping("/get_projects")
    public Result<List<PmProject>> getProjects(@RequestParam(value = "id") String userId){
        List<PmProject> projectList = projectUserService.getProjectsByUserId(userId);
        return Result.success(projectList);
    }

    @GetMapping("/get_project")
    public Result<PmProject> getProject(@RequestParam(value = "id") Long projectId){
        PmProject project = projectService.getById(projectId);
        return Result.success(project);
    }

    @GetMapping("/search_project")
    public Result<List<PmProject>> searchProjects(@RequestParam(value = "userId") String userId,
                                                  @RequestParam(value = "key") String key){
        List<PmProject> projectList = projectService.searchProjects(userId,key);
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

        UmUserRole userRole = new UmUserRole();
        userRole.setUserId(project.getCreateUserId());
        userRole.setProjectId(project.getId());
        userRole.setRole(1);
        boolean isSave3 = userRoleService.save(userRole);

        if (isSave && isSave2 && isSave3){
            return Result.success(project);
        }
        return Result.failed();
    }

    @GetMapping("/get_project_members")
    public Result<List<MemberParam>> getProjectMembers(@RequestParam(value = "projectId") String id){
        List<MemberParam> memberList = projectUserService.getMemberListByProjectId(id);
        return Result.success(memberList);
    }

    @PostMapping("/update_cover")
    public Result<String> updateCover(@RequestBody PmProject project){
        UpdateWrapper<PmProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",project.getId()).set("picture",project.getPicture());
        boolean isUpdate = projectService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/update_name")
    public Result<String> updateName(@RequestBody PmProject project){
        UpdateWrapper<PmProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",project.getId()).set("name",project.getName());
        boolean isUpdate = projectService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/update_description")
    public Result<String> updateDescription(@RequestBody PmProject project){
        UpdateWrapper<PmProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",project.getId()).set("description",project.getDescription());
        boolean isUpdate = projectService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/archive_project")
    public Result<String> archiveProject(@RequestParam(value = "projectId") Long id){
        UpdateWrapper<PmProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("status",0);
        boolean isUpdate = projectService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/delete_project")
    public Result<String> deleteProject(@RequestParam(value = "projectId") Long id){
        boolean isDelete = projectService.removeById(id);
        if(isDelete){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/search")
    public Result<PmProject> search(@RequestParam(value = "projectId") String id){
        PmProject project = projectService.getById(id);
        return Result.success(project);
    }

    @PostMapping("/join_project")
    public Result<String> joinProject(@RequestBody PmProjectUser projectUser){
        QueryWrapper<PmProjectUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectUser.getProjectId())
                .eq("user_id",projectUser.getUserId());
        PmProjectUser pu = projectUserService.getOne(queryWrapper);

        UmUserRole userRole = new UmUserRole();
        userRole.setProjectId(projectUser.getProjectId());
        userRole.setUserId(projectUser.getUserId());
        userRole.setRole(3);
        userRoleService.save(userRole);

        if(pu==null){
            projectUserService.save(projectUser);
            return Result.success("加入成功");
        }
        return Result.success("你已经加入了该项目");
    }

    @GetMapping("/get_invitations")
    public Result<List<InvitationParam>> getInvitations(@RequestParam(value = "userId") String userId){
        QueryWrapper<PmUserInvite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("status",1);
        List<PmUserInvite> list = userInviteService.list(queryWrapper);
        List<InvitationParam> list1 = new ArrayList<>();
        for(PmUserInvite userInvite : list){
            InvitationParam param = new InvitationParam();
            param.setId(userInvite.getId());
            PmProject project = projectService.getById(userInvite.getProjectId());
            UmUser inviter = userService.getById(userInvite.getInviterId());
            param.setContent(inviter.getNickName()+"邀请您加入项目【"+project.getName()+"】");
            list1.add(param);
        }
        return Result.success(list1);
    }

    @GetMapping("/agree_invite")
    public Result<String> agreeInvite(@RequestParam(value = "id") String id){
        PmUserInvite userInvite = userInviteService.getById(id);

        UpdateWrapper<PmUserInvite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("status",2);
        userInviteService.update(updateWrapper);

        PmProjectUser projectUser = new PmProjectUser();
        projectUser.setUserId(userInvite.getUserId());
        projectUser.setProjectId(userInvite.getProjectId());
        projectUserService.save(projectUser);

        UmUserRole userRole = new UmUserRole();
        userRole.setProjectId(userInvite.getProjectId());
        userRole.setUserId(userInvite.getUserId());
        userRole.setRole(3);
        userRoleService.save(userRole);

        return Result.success("success");
    }

    @GetMapping("/refuse_invite")
    public Result<String> refuseInvite(@RequestParam(value = "id") String id){
        UpdateWrapper<PmUserInvite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("status",2);
        userInviteService.update(updateWrapper);
        return Result.success("success");
    }

    @GetMapping("/get_user_role")
    public Result<Integer> getUserRole(@RequestParam(value = "projectId") String projectId,
                                      @RequestParam(value = "userId") String userId){
        QueryWrapper<UmUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId).eq("user_id",userId);
        UmUserRole userRole = userRoleService.getOne(queryWrapper);
        return Result.success(userRole.getRole());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void getNumberOfUndoneTasks(){
        List<PmProject> projects = projectService.list();
        for(PmProject project : projects){
            int number = 0;
            List<TmTask> tasks = projectService.getTasksByProjectId(project.getId());
            for(TmTask task : tasks){
                if(task.getStatus()!=3){
                    number++;
                }
            }
            PmTaskUndone taskUndone = new PmTaskUndone();
            taskUndone.setNumber(number);
            taskUndone.setProjectId(project.getId());
            taskUndone.setDate(LocalDate.now());
            taskUndoneService.save(taskUndone);
        }
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
