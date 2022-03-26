package com.yu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yu.common.api.Result;
import com.yu.dto.*;
import com.yu.entity.*;
import com.yu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
@RequestMapping("task")
public class TmTaskController {
    @Autowired
    TmTaskService taskService;
    @Autowired
    TmBoardService boardService;
    @Autowired
    PmProjectBoardService projectBoardService;
    @Autowired
    TmBoardTaskService boardTaskService;
    @Autowired
    TmTagService tagService;
    @Autowired
    UmUserService userService;
    @Autowired
    TmTaskFollowerService taskFollowerService;
    @Autowired
    TmTaskCommentService taskCommentService;

    @PostMapping("/add_task")
    public Result<TmTask> addTask(@RequestBody AddTaskParam taskParam){
        TmTask task = new TmTask();
        task.setDescription(taskParam.getDescription());
        task.setStartDate(LocalDateTime.now());
        task.setType(1);
        task.setPriority(1);
        task.setStatus(1);
        boolean isSave = taskService.save(task);

        TmBoardTask boardTask = new TmBoardTask();
        boardTask.setBoardId(taskParam.getBoardId());
        boardTask.setTaskId(task.getId());
        boolean isSave2 = boardTaskService.save(boardTask);
        if (isSave && isSave2){
            return Result.success(task);
        }
        return Result.failed();
    }

    @GetMapping("/get_tasks")
    public Result<List<TmTask>> getTasks(@RequestParam("boardId") String id){
        List<TmTask> list = taskService.getTasksByBoardId(id);
        return Result.success(list);
    }

    @PostMapping("/add_board")
    public Result<TmBoard> addList(@RequestBody AddListParam listParam){
        TmBoard board = new TmBoard();
        board.setName(listParam.getName());
        boolean isSave = boardService.save(board);
        if (isSave){
            PmProjectBoard projectBoard = new PmProjectBoard();
            projectBoard.setBoardId(board.getId());
            projectBoard.setProjectId(listParam.getProjectId());
            boolean isSave2 = projectBoardService.save(projectBoard);
            if(isSave2){
                return Result.success(board);
            }
        }
        return Result.failed();
    }

    @GetMapping("/get_boards")
    public Result<List<TmBoard>> getBoards(@RequestParam(value = "projectId") String projectId){
        List<TmBoard> list = projectBoardService.getBoardsByProjectId(projectId);
        return Result.success(list);
    }

    @GetMapping("/delete_board")
    public Result<String> deleteBoard(@RequestParam(value = "id") String boardId){
        QueryWrapper<PmProjectBoard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("board_id",boardId);
        boolean isMove = projectBoardService.remove(queryWrapper);
        boolean isMove2 = boardService.removeById(boardId);

        if (isMove && isMove2) {
            return Result.success("success");
        }
        return Result.failed();
    }


    @GetMapping("/get_task_info")
    public Result<TaskInfoParam> getTaskInfo(@RequestParam(value = "taskId") String taskId){
        TmTask task = taskService.getById(taskId);

        QueryWrapper<TmTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        List<TmTag> list = tagService.list(queryWrapper);

        List<MemberParam> followers = taskFollowerService.getFollowersByTaskId(taskId);

        UmUser user = userService.getById(task.getExecutor());

        TaskInfoParam taskInfo = new TaskInfoParam(
                task.getDescription(),
                user,
                task.getStartDate(),
                task.getDueDate(),
                task.getPriority(),
                task.getType(),
                list,
                followers,
                task.getStatus()
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

    @PostMapping("/change_start_date")
    public Result<String> changeStartDate(@RequestBody ChangeDateParam date){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",date.getId()).set("start_date",date.getDate());
        boolean isUpdate = taskService.update(updateWrapper);
        if(isUpdate){
            return Result.success("success");
        }
        return Result.failed();
    }

    @PostMapping("/change_due_date")
    public Result<String> changeDueDate(@RequestBody ChangeDateParam date){
        UpdateWrapper<TmTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",date.getId()).set("due_date",date.getDate());
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
}

