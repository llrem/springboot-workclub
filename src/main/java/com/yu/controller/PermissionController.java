package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yu.common.api.Result;
import com.yu.dto.MemberParam;
import com.yu.dto.PermissionParam;
import com.yu.entity.PmProjectUser;
import com.yu.entity.PmUserInvite;
import com.yu.entity.UmUser;
import com.yu.entity.UmUserRole;
import com.yu.service.PmProjectUserService;
import com.yu.service.PmUserInviteService;
import com.yu.service.UmUserRoleService;
import com.yu.service.UmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project/member")
public class PermissionController {
    @Autowired
    PmProjectUserService projectUserService;
    @Autowired
    UmUserRoleService userRoleService;
    @Autowired
    UmUserService userService;
    @Autowired
    PmUserInviteService userInviteService;

    @GetMapping("/get_members")
    public Result<List<PermissionParam>> getMembers(@RequestParam(value = "projectId") String projectId){
        List<MemberParam> memberParamList = projectUserService.getMemberListByProjectId(projectId);
        List<PermissionParam> permissionParamList = new ArrayList<>();
        for(MemberParam memberParam : memberParamList){
            PermissionParam permissionParam = new PermissionParam();

            permissionParam.setId(memberParam.getId());
            permissionParam.setName(memberParam.getNickName());
            permissionParam.setIcon(memberParam.getIcon());

            QueryWrapper<UmUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",memberParam.getId())
                    .eq("project_id",projectId);
            UmUserRole userRole = userRoleService.getOne(queryWrapper);
            permissionParam.setRole(userRole.getRole());

            permissionParamList.add(permissionParam);
        }
        return Result.success(permissionParamList);
    }


    @GetMapping("/set_permission")
    public Result<String> setPermission(@RequestParam(value = "userId") String userId,
                                @RequestParam(value = "projectId") String projectId,
                                @RequestParam(value = "role") String role){
        UpdateWrapper<UmUserRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id",userId)
                .eq("project_id",projectId)
                .set("role",role);
        userRoleService.update(updateWrapper);
        return Result.success("success");
    }

    @GetMapping("/remove_member")
    public Result<String> removeMember(@RequestParam(value = "userId") String userId,
                                        @RequestParam(value = "projectId") String projectId){
        QueryWrapper<PmProjectUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId).eq("user_id",userId);
        projectUserService.remove(queryWrapper);

        QueryWrapper<UmUserRole> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("project_id",projectId).eq("user_id",userId);
        userRoleService.remove(queryWrapper1);
        return Result.success("success");
    }

    @GetMapping("/search_member")
    public Result<List<PermissionParam>> searchMember(@RequestParam(value = "keyword") String keyword,
                                       @RequestParam(value = "projectId") String projectId){
        List<MemberParam> memberParamList = projectUserService.searchMember(keyword,projectId);
        List<PermissionParam> permissionParamList = new ArrayList<>();
        for(MemberParam memberParam : memberParamList){
            PermissionParam permissionParam = new PermissionParam();

            permissionParam.setId(memberParam.getId());
            permissionParam.setName(memberParam.getNickName());
            permissionParam.setIcon(memberParam.getIcon());

            QueryWrapper<UmUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",memberParam.getId())
                    .eq("project_id",projectId);
            UmUserRole userRole = userRoleService.getOne(queryWrapper);
            permissionParam.setRole(userRole.getRole());

            permissionParamList.add(permissionParam);
        }
        return Result.success(permissionParamList);
    }


    @GetMapping("/search_user")
    public Result<List<MemberParam>> searchMember(@RequestParam(value = "keyword") String keyword){
        QueryWrapper<UmUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",keyword);
        List<UmUser> list = userService.list(queryWrapper);
        List<MemberParam> memberParamList = new ArrayList<>();
        for(UmUser user : list){
            MemberParam param = new MemberParam();
            param.setId(user.getId());
            param.setIcon(user.getIcon());
            param.setNickName(user.getNickName());
            memberParamList.add(param);
        }
        return Result.success(memberParamList);
    }

    @PostMapping("/invite_user")
    public Result<String> inviteUser(@RequestBody PmUserInvite userInvite){
        userInvite.setStatus(1);
        userInviteService.save(userInvite);
        return Result.success("success");
    }
}
