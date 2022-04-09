package com.yu.controller;

import com.yu.common.api.Result;
import com.yu.dto.MemberParam;
import com.yu.dto.PermissionParam;
import com.yu.service.PmProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("task/member")
public class PermissionController {
    @Autowired
    PmProjectUserService projectUserService;

    @GetMapping("/get_members")
    public Result<List<PermissionParam>> getMembers(@RequestParam(value = "projectId") Long projectId){
        List<MemberParam> memberParamList = projectUserService.getMemberListByProjectId(projectId);
        List<PermissionParam> permissionParamList = new ArrayList<>();
        for(MemberParam memberParam : memberParamList){
            PermissionParam permissionParam = new PermissionParam();

            permissionParam.setId(memberParam.getId());
            permissionParam.setName(memberParam.getNickName());
            permissionParam.setIcon(memberParam.getIcon());
            List<String> list = projectUserService.getPermissionByUserId(memberParam.getId());
            StringBuilder permission = new StringBuilder();
            for (int i=0; i<list.size(); i++) {
                permission.append(list.get(i));
                if(i!=list.size()-1){
                    permission.append(", ");
                }
            }
            permissionParam.setRoles(permission.toString());
            permissionParamList.add(permissionParam);
        }
        return Result.success(permissionParamList);
    }
}
