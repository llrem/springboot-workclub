package com.yu.dto;

import com.yu.entity.TmTask;
import com.yu.entity.TmTaskFollower;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskClassifyByStatusParam {
    List<TmTask> taskStatus1 = new ArrayList<>();
    List<TmTask> taskStatus2 = new ArrayList<>();
    List<TmTask> taskStatus3 = new ArrayList<>();

    public void addTaskStatus1(TmTask task){
        taskStatus1.add(task);
    }
    public void addTaskStatus2(TmTask task){
        taskStatus2.add(task);
    }
    public void addTaskStatus3(TmTask task){
        taskStatus3.add(task);
    }
}
