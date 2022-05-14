package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.common.api.Result;
import com.yu.dto.CardDataParam;
import com.yu.dto.PieDataParam;
import com.yu.entity.PmProjectBoard;
import com.yu.service.PmProjectBoardService;
import com.yu.service.PmProjectService;
import com.yu.service.PmProjectUserService;
import com.yu.service.TmBoardTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project/statistic")
public class statisticController {

    @Autowired
    PmProjectBoardService projectBoardService;
    @Autowired
    TmBoardTaskService boardTaskService;
    @Autowired
    PmProjectUserService projectUserService;
    @Autowired
    PmProjectService projectService;

    @GetMapping("/get_pie_data")
    public Result<List<PieDataParam>> getPieData(@RequestParam(value = "projectId") String projectId){
        QueryWrapper<PmProjectBoard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId);
        List<PmProjectBoard>  boards = projectBoardService.list(queryWrapper);
        List<PieDataParam> params = new ArrayList<>();
        for(PmProjectBoard board : boards){
            PieDataParam param = boardTaskService.getBoardData(board.getBoardId());
            params.add(param);
        }
        return Result.success(params);
    }

    @GetMapping("/get_card_data")
    public Result<List<CardDataParam>> getCardData(@RequestParam(value = "projectId") String projectId){
        List<CardDataParam> list = new ArrayList<>();

        List<CardDataParam> typeList = boardTaskService.numberOfType(projectId);
        int Total = 0;
        for(CardDataParam param : typeList){
            Total += param.getValue();
        }
        for(CardDataParam param : typeList){
            if(param.getName().equals("1")){
                param.setName("任务");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#3282B8");
            }
            if(param.getName().equals("2")) {
                param.setName("里程碑");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#1E5F74");
            }
            if(param.getName().equals("3")) {
                param.setName("问题");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#F05454");
            }
        }
        List<CardDataParam> statusList = boardTaskService.numberOfStatus(projectId);
        for(CardDataParam param : statusList){
            if(param.getName().equals("1")) {
                param.setName("未完成");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#BBBBBB");
            }
            if(param.getName().equals("2")) {
                param.setName("进行中");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#1597BB");
            }
            if(param.getName().equals("3")) {
                param.setName("已完成");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#4E9F3D");
            }
        }
        List<CardDataParam> priorityList = boardTaskService.numberOfPriority(projectId);
        CardDataParam cardDataParam = null;
        for(CardDataParam param : priorityList){
            if(param.getName().equals("1")) {
                cardDataParam = param;
            }
            if(param.getName().equals("2")) {
                param.setName("紧急");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#FFA500");
            }
            if(param.getName().equals("3")) {
                param.setName("非常紧急");
                param.setPercentage(param.getValue() * 100 / Total);
                param.setColor("#ff4500");
            }
        }
        priorityList.remove(cardDataParam);

        list.add(new CardDataParam("总计",Total,100,"#333333"));
        list.addAll(typeList);
        list.addAll(statusList);
        list.addAll(priorityList);
        return Result.success(list);
    }

    @GetMapping("/get_projects_data")
    public Result<List<PieDataParam>> getProjectsData(@RequestParam(value = "userId") String userId){
        List<PieDataParam> list = projectService.getProjectsData(userId);
        for(PieDataParam param : list){
            if(param.getName().equals("1")){
                param.setName("进行中项目");
            }else{
                param.setName("已归档项目");
            }
        }
        return Result.success(list);
    }

    @GetMapping("/get_tasks_data")
    public Result<List<String>> getTasksData(@RequestParam(value = "userId") String userId){
        List<String> list = new ArrayList<>();
        List<String> list1 = projectService.getStatusData(userId);
        List<String> list2 = projectService.getPriorityData(userId);
        list2.remove(0);
        list.addAll(list1);
        list.addAll(list2);
        return Result.success(list);
    }
}
