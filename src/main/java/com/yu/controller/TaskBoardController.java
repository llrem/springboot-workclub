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
@RequestMapping("/project/board")
public class TaskBoardController {
    @Autowired
    TmTaskService taskService;
    @Autowired
    TmBoardService boardService;
    @Autowired
    PmProjectBoardService projectBoardService;
    @Autowired
    TmBoardTaskService boardTaskService;


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
    public Result<List<TmTask>> getTasks(@RequestParam("boardId") String id,
                                         @RequestParam("keyword") String keyword){
        List<TmTask> list = taskService.getTasksByBoardId(id,keyword);
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

    @GetMapping("/board_rename")
    public Result<String> boardRename(@RequestParam(value = "boardId") String boardId,
                                      @RequestParam(value = "boardName") String boardName){
        UpdateWrapper<TmBoard> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",boardId).set("name",boardName);
        boardService.update(updateWrapper);
        return Result.success("success");
    }
}

