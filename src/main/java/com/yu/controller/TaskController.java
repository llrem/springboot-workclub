package com.yu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yu.common.api.Result;
import com.yu.common.graph.AdjacencyList;
import com.yu.common.graph.ArcNode;
import com.yu.common.graph.GraphArcNode;
import com.yu.common.graph.VexNode;
import com.yu.common.hashing.Hash;
import com.yu.dto.*;
import com.yu.entity.*;
import com.yu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llrem
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TmTaskService taskService;
    @Autowired
    TmTagService tagService;
    @Autowired
    UmUserService userService;
    @Autowired
    TmTaskFollowerService taskFollowerService;
    @Autowired
    TmTaskCommentService taskCommentService;
    @Autowired
    TmTaskFileService taskFileService;
    @Autowired
    TmTaskLogService taskLogService;
    @Autowired
    UmUserEventService userEventService;
    @Autowired
    TmTaskSubService subTaskService;
    @Autowired
    TmTaskDependenceService taskDependenceService;
    @Autowired
    PmProjectService projectService;

    @GetMapping("/get_task_info")
    public Result<TaskInfoParam> getTaskInfo(@RequestParam(value = "taskId") String taskId){
        TmTask task = taskService.getById(taskId);
        QueryWrapper<TmTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        List<TmTag> list = tagService.list(queryWrapper);
        List<MemberParam> followers = taskFollowerService.getFollowersByTaskId(taskId);
        UmUser user = userService.getById(task.getExecutor());
        List<TaskParam> taskList = taskDependenceService.getHeadTaskList(taskId);

        TaskInfoParam taskInfo = new TaskInfoParam(
                task.getDescription(),
                user,
                task.getStartDate(),
                task.getDueDate(),
                task.getPriority(),
                task.getType(),
                list,
                followers,
                task.getStatus(),
                taskList
        );

        return Result.success(taskInfo);
    }

    @GetMapping("/change_task_status")
    public Result<String> changeTaskStatus(@RequestParam(value = "taskId") Long id,
                                   @RequestParam(value = "status") String status){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("status",status);
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/change_task_priority")
    public Result<String> changeTaskPriority(@RequestParam(value = "taskId") Long id,
                                     @RequestParam(value = "priority") String priority){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("priority",priority);
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/change_task_type")
    public Result<String> changeTaskType(@RequestParam(value = "taskId") Long id,
                                             @RequestParam(value = "type") String type){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("type",type);
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/change_start_date")
    public Result<String> changeStartDate(@RequestBody ChangeDateParam param){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",param.getId()).set("start_date",param.getDate());
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/change_due_date")
    public Result<String> changeDueDate(@RequestBody ChangeDateParam param){
        if(param.getDate()!=null){
            QueryWrapper<UmUserEvent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_id",param.getId());
            List<UmUserEvent> userEventList = userEventService.list(queryWrapper);
            TmTask task = taskService.getById(param.getId());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(userEventList.size()==0){
                QueryWrapper<TmTaskFollower> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("task_id",param.getId());
                List<TmTaskFollower> list = taskFollowerService.list(queryWrapper2);
                for(TmTaskFollower taskFollower:list){
                    UmUserEvent userEvent = new UmUserEvent();
                    userEvent.setUserId(taskFollower.getUserId());
                    userEvent.setTaskId(param.getId());
                    userEvent.setTitle("任务到期提醒");
                    userEvent.setContent("任务 ["+task.getDescription()+"] 即将到期, 截至时间 "+dateTimeFormatter.format(param.getDate()));
                    userEvent.setDate(param.getDate().toLocalDate());
                    userEventService.save(userEvent);
                }
            }else{
                QueryWrapper<TmTaskFollower> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("task_id",param.getId());
                List<TmTaskFollower> list = taskFollowerService.list(queryWrapper3);
                for(TmTaskFollower taskFollower:list){
                    UpdateWrapper<UmUserEvent> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("user_id",taskFollower.getUserId())
                            .eq("task_id",param.getId())
                            .set("content","任务 ["+task.getDescription()+"] 即将到期, 截至时间 "+dateTimeFormatter.format(param.getDate()))
                            .set("date",param.getDate().toLocalDate());
                    userEventService.update(updateWrapper);
                }

            }
        }else{
            QueryWrapper<UmUserEvent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_id",param.getId());
            userEventService.remove(queryWrapper);
        }

        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",param.getId()).set("due_date",param.getDate());
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/add_tag")
    public Result<List<TmTag>> addTag(@RequestBody TmTag tag){
        boolean isSave = tagService.save(tag);
        QueryWrapper<TmTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",tag.getTaskId());
        List<TmTag> list = tagService.list(queryWrapper);
        if(isSave){
            return Result.success(list);
        }
        return Result.failed();
    }

    @PostMapping("/delete_tag")
    public Result<List<TmTag>> deleteTag(@RequestBody TmTag tag){
        boolean isDelete = tagService.removeById(tag.getId());
        QueryWrapper<TmTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",tag.getTaskId());
        List<TmTag> list = tagService.list(queryWrapper);
        if(isDelete){
            return Result.success(list);
        }
        return Result.failed();
    }

    @GetMapping("/select_executor")
    public Result<UmUser> selectExecutor(@RequestParam(value = "taskId") Long taskId,
                                            @RequestParam(value = "memberId") Long memberId){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",taskId).set("executor",memberId);
        boolean isUpdate = taskService.update(updateWrapper);

        if (isUpdate){
            return Result.success(userService.getById(memberId));
        }
        return Result.failed();
    }

    @GetMapping("/select_follower")
    public Result<UmUser> selectFollower(@RequestParam(value = "taskId") Long taskId,
                                         @RequestParam(value = "memberId") Long memberId){
        TmTaskFollower taskFollower = new TmTaskFollower();
        taskFollower.setTaskId(taskId);
        taskFollower.setUserId(memberId);
        boolean isSave = taskFollowerService.save(taskFollower);
        if (isSave){
            return Result.success(userService.getById(memberId));
        }
        return Result.failed();
    }

    @GetMapping("/remove_executor")
    public Result<String> removeExecutor(@RequestParam(value = "taskId") Long taskId){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",taskId).set("executor",null);
        boolean isUpdate = taskService.update(updateWrapper);
        if (isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @GetMapping("/change_description")
    public Result<String> changeDescription(@RequestParam(value = "taskId") Long taskId,
                                            @RequestParam(value = "description") String description){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",taskId).set("description",description);
        boolean isUpdate = taskService.update(updateWrapper);
        if (isUpdate){
            return Result.success(description);
        }
        return Result.failed();
    }

    @GetMapping("/get_comments")
    public Result<List<CommentParam>> getComments(@RequestParam(value = "taskId") Long taskId){
        List<CommentParam> list = taskCommentService.getCommentsByTaskId(taskId);
        return Result.success(list);
    }

    @PostMapping("/submit_comments")
    public Result<TmTaskComment> submitComments(@RequestBody TmTaskComment comment){
        comment.setCreateDate(LocalDateTime.now());
       boolean isSave = taskCommentService.save(comment);
       if(isSave){
           return Result.success(comment);
       }
       return Result.failed();
    }

    @GetMapping("/get_files")
    public Result<List<TmTaskFile>> getFiles(@RequestParam(value = "taskId") Long taskId){
        QueryWrapper<TmTaskFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        List<TmTaskFile> list = taskFileService.list(queryWrapper);
        return Result.success(list);
    }

    @PostMapping("/add_file")
    public Result<String> addFile(@RequestBody TmTaskFile taskFile){
        QueryWrapper<TmTaskFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path",taskFile.getPath()).eq("name",taskFile.getName());
        List<TmTaskFile> list = taskFileService.list(queryWrapper);
        if(list.size()>0){
            return Result.success("success");
        }
       boolean isSave = taskFileService.save(taskFile);
       if(isSave){
           return Result.success("success");
       }
       return Result.failed();
    }

    @PostMapping("/add_log")
    public Result<TmTaskLog> addLog(@RequestBody TmTaskLog taskLog){
        taskLog.setCreateDate(LocalDateTime.now());
        boolean isSave = taskLogService.save(taskLog);
        if(isSave){
            return Result.success(taskLog);
        }
        return Result.failed();
    }

    @GetMapping("/get_logList")
    public Result<List<TaskLogListItem>> getLogList(@RequestParam(value = "taskId") String taskId){
        List<TaskLogParam> logParamList = taskLogService.getLogsByTaskId(taskId);
        List<TaskLogListItem> list = new ArrayList<>();
        for(TaskLogParam logParam : logParamList){
            TaskLogListItem logListItem = new TaskLogListItem();
            switch (logParam.getType()){
                case 1:{
                    logListItem.setContent(logParam.getName()+" 将任务描述修改为 "+logParam.getObject());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 2:{
                    logListItem.setContent(logParam.getName()+" 将任务状态修改为 "+logParam.getObject());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 3:{
                    UmUser user = userService.getById(logParam.getObject());
                    logListItem.setContent(logParam.getName()+" 将任务负责人更改为 "+user.getNickName());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 4:{
                    if(logParam.getObject()!=null){
                        logListItem.setContent(logParam.getName()+" 将任务开始时间更改为 "+logParam.getObject());
                    }else{
                        logListItem.setContent(logParam.getName()+" 取消了任务开始时间");
                    }
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 5:{
                    if(logParam.getObject()!=null){
                        logListItem.setContent(logParam.getName()+" 将任务结束时间更改为 "+logParam.getObject());
                    }else{
                        logListItem.setContent(logParam.getName()+" 取消了任务结束时间");
                    }
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 6:{
                    logListItem.setContent(logParam.getName()+" 将任务优先级更改为 "+logParam.getObject());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 7:{
                    logListItem.setContent(logParam.getName()+" 添加标签 ["+logParam.getObject()+"]");
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 8:{
                    logListItem.setContent(logParam.getName()+" 删除标签 ["+logParam.getObject()+"]");
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 9:{
                    UmUser user = userService.getById(logParam.getObject());
                    logListItem.setContent(logParam.getName()+" 添加参与人 "+user.getNickName());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
                case 10:{
                    logListItem.setContent(logParam.getName()+" 将类型修改为 "+logParam.getObject());
                    logListItem.setCreateDate(logParam.getCreateDate());
                    list.add(logListItem);
                    break;
                }
            }
        }
        return Result.success(list);
    }

    @GetMapping("/delete_task")
    public Result<String> deleteTask(@RequestParam(value = "taskId") Long taskId){
        boolean isDelete = taskService.removeById(taskId);
        if(isDelete){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/publish_sub_task")
    public Result<TmTaskSub> subTask(@RequestBody TmTaskSub subTask){
        subTask.setCreateDate(LocalDateTime.now());
        subTaskService.save(subTask);
        return Result.success(subTask);
    }

    @GetMapping("/get_sub_tasks")
    public Result<List<SubTaskParam>> getSubTasks(@RequestParam(value = "taskId") String taskId){
        List<SubTaskParam> list = subTaskService.getSubTasks(taskId);
        return Result.success(list);
    }

    @GetMapping("/delete_sub_task")
    public Result<String> deleteSubTask(@RequestParam(value = "id") String id){
       subTaskService.removeById(id);
        return Result.success("success");
    }

    @GetMapping("/get_head_tasks")
    public Result<List<TaskParam>> getHeadTasks(@RequestParam(value = "taskId") String taskId){
        List<TaskParam> taskList = taskDependenceService.getHeadTaskList(taskId);
        return Result.success(taskList);
    }

    @GetMapping("/add_head_task")
    public Result<String> addHeadTask(@RequestParam(value = "projectId") String projectId,
                                      @RequestParam(value = "headTask") Long headTask,
                                      @RequestParam(value = "rearTask") Long rearTask){
        TmTaskDependence taskDependence = new TmTaskDependence(headTask,rearTask);
        if(taskDependence.getHeadTask().equals(taskDependence.getRearTask())){
            return Result.failed("不可添加自己");
        }
        taskDependenceService.save(taskDependence);

        //判度该任务是否和其他任务形成了环
        List<TmTaskDependence> arcs = projectService.getAssociatedTasks(projectId);
        HashMap<Integer,String> hashMap = new HashMap<>();
        List<Long> vexNodes = new ArrayList<>();
        for(TmTaskDependence arc : arcs){
            Long source = arc.getHeadTask();
            Long target = arc.getRearTask();
            if(!hashMap.containsKey(Math.toIntExact(source))){
                hashMap.put(Math.toIntExact(source),"");
                vexNodes.add(source);
            }
            if(!hashMap.containsKey(Math.toIntExact(target))){
                hashMap.put(Math.toIntExact(target),"");
                vexNodes.add(target);
            }
        }
        int vexnum = hashMap.size();
        //顶点放入散列表
        Hash hash = new Hash(vexnum);
        for(Long key : vexNodes){
            hash.put(Math.toIntExact(key));
        }

        AdjacencyList adjList = new AdjacencyList(vexnum,arcs.size());
        for(Long key : vexNodes){
            String taskName = taskService.getById(key).getDescription();
            VexNode vexNode = new VexNode(taskName);
            adjList.addVexNode(hash.getIndexForKey(Math.toIntExact(key)),vexNode);
        }
        for(TmTaskDependence arc : arcs){
            ArcNode arcNode = new ArcNode(hash.getIndexForKey(Math.toIntExact(arc.getRearTask())));
            adjList.addArcNode(hash.getIndexForKey(Math.toIntExact(arc.getHeadTask())),arcNode);
        }

        if(adjList.isCyclic()){
            taskDependenceService.removeById(taskDependence.getId());
            return Result.failed("该任务和其他任务形成了循环依赖！");
        }
        return Result.success("success");
    }

    @PostMapping("/remove_head_task")
    public Result<String> removeHeadTask(@RequestBody TmTaskDependence taskDependence){
        QueryWrapper<TmTaskDependence> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("head_task",taskDependence.getHeadTask())
                .eq("rear_task",taskDependence.getRearTask());
        taskDependenceService.remove(queryWrapper);
        return Result.success("success");
    }
}

