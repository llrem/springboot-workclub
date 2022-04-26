package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yu.common.api.Result;
import com.yu.dto.TaskClassifyByStatusParam;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskFollower;
import com.yu.entity.UmUserEvent;
import com.yu.service.TmTaskFollowerService;
import com.yu.service.UmUserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    TmTaskFollowerService taskFollowerService;
    @Autowired
    UmUserEventService userEventService;

    @GetMapping("/get_tasks")
    public Result<TaskClassifyByStatusParam> getCategoricalTasks(@RequestParam(value = "userId") String userId){
        List<TmTask> list = taskFollowerService.getTaskByUserId(userId);
        TaskClassifyByStatusParam param = new TaskClassifyByStatusParam();
        for(TmTask task : list){
            switch (task.getStatus()){
                case 1 : {
                    param.addTaskStatus1(task);
                    break;
                }
                case 2 : {
                    param.addTaskStatus2(task);
                    break;
                }
                case 3 : {
                    param.addTaskStatus3(task);
                    break;
                }
            }
        }
        return Result.success(param);
    }

    @GetMapping("/get_event_dates")
    public Result<List<String>> getEventDates(@RequestParam(value = "userId") String userId){
        QueryWrapper<UmUserEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UmUserEvent> list = userEventService.list(queryWrapper);
        List<String> dateList = new ArrayList<>();
        for (UmUserEvent umUserEvent : list) {
            dateList.add(umUserEvent.getDate().toString());
        }
        return Result.success(dateList);
    }

    @GetMapping("/get_events")
    public Result<List<UmUserEvent>> getEvents(@RequestParam(value = "userId") String userId,
                                               @RequestParam(value = "date")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        QueryWrapper<UmUserEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("date",date);
        List<UmUserEvent> list = userEventService.list(queryWrapper);
        return Result.success(list);
    }
}
