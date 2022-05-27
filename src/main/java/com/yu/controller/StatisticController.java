package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.common.api.Result;
import com.yu.common.graph.*;
import com.yu.common.hashing.Hash;
import com.yu.dto.CardDataParam;
import com.yu.dto.LineDataParam;
import com.yu.dto.PieDataParam;
import com.yu.dto.TopologyGraphParam;
import com.yu.entity.PmProjectBoard;
import com.yu.entity.PmTaskUndone;
import com.yu.entity.TmTaskDependence;
import com.yu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/project/statistic")
public class StatisticController {

    @Autowired
    PmProjectBoardService projectBoardService;
    @Autowired
    TmBoardTaskService boardTaskService;
    @Autowired
    PmProjectUserService projectUserService;
    @Autowired
    PmProjectService projectService;
    @Autowired
    TmTaskService taskService;
    @Autowired
    PmTaskUndoneService taskUndoneService;

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
        if(list2.size()>0){
            list2.remove(0);
        }
        list.addAll(list1);
        list.addAll(list2);
        return Result.success(list);
    }

    @GetMapping("/get_line_data")
    public Result<LineDataParam> getLineData(@RequestParam(value = "projectId") String projectId){
        QueryWrapper<PmTaskUndone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId)
                .orderBy(true,true,"date")
                .last("LIMIT 15");
        List<PmTaskUndone> list = taskUndoneService.list(queryWrapper);

        List<String> dates = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for(PmTaskUndone taskUndone : list){
            LocalDate date = taskUndone.getDate();
            dates.add(date.getMonthValue()+"-"+date.getDayOfMonth());
            numbers.add(taskUndone.getNumber());
        }
        LineDataParam param = new LineDataParam();
        param.setDates(dates);
        param.setNumbers(numbers);
        return Result.success(param);
    }

    @GetMapping("/get_topology")
    public Result<TopologyGraphParam> getTopology(@RequestParam(value = "projectId") String projectId){
        List<TmTaskDependence> arcs = projectService.getAssociatedTasks(projectId);
        List<GraphArcNode> graphArcNodes = new ArrayList<>();
        //获取不重复的顶点
        HashMap<Integer,String> hashMap = new HashMap<>();
        List<Long> vexNodes = new ArrayList<>();
        for(TmTaskDependence taskDependence : arcs){
            Long headTask = taskDependence.getHeadTask();
            Long rearTask = taskDependence.getRearTask();
            String headTaskName = taskService.getById(headTask).getDescription();
            String rearTaskName = taskService.getById(rearTask).getDescription();
            GraphArcNode graphArcNode = new GraphArcNode(headTaskName,rearTaskName);
            graphArcNodes.add(graphArcNode);
            if(!hashMap.containsKey(Math.toIntExact(headTask))){
                hashMap.put(Math.toIntExact(headTask),"");
                vexNodes.add(headTask);
            }
            if(!hashMap.containsKey(Math.toIntExact(rearTask))){
                hashMap.put(Math.toIntExact(rearTask),"");
                vexNodes.add(rearTask);
            }
        }
        //顶点放入散列表
        Hash hash = new Hash(vexNodes.size());
        for(Long key : vexNodes){
            hash.put(Math.toIntExact(key));
        }
        //创建图邻接表
        AdjacencyList adjList = new AdjacencyList(vexNodes.size(),arcs.size());
        for(Long key : vexNodes){
            String taskName = taskService.getById(key).getDescription();
            VexNode vexNode = new VexNode(taskName);
            adjList.addVexNode(hash.getIndexForKey(Math.toIntExact(key)),vexNode);
        }
        for(TmTaskDependence taskDependence : arcs){
            ArcNode arcNode = new ArcNode(hash.getIndexForKey(Math.toIntExact(taskDependence.getRearTask())));
            adjList.addArcNode(hash.getIndexForKey(Math.toIntExact(taskDependence.getHeadTask())),arcNode);
        }
        //输出拓扑排序
        List<GraphVexNode> topSort = TopSort(adjList);
        if(topSort==null){
            return Result.failed("任务拓扑序列存在环！");
        }

        TopologyGraphParam param = new TopologyGraphParam(topSort,graphArcNodes);
        return Result.success(param);
    }

    //拓扑排序
    public List<GraphVexNode> TopSort(AdjacencyList adjList){
        Queue<VexNode> queue = new LinkedList<>();
        VexNode[] vexNodes =  adjList.getVexNodes();
        int counter=0;
        //顶点入度数组
        int[] Indegree = new int[adjList.getVexNodes().length];
        for (VexNode vexNode : vexNodes) {
            ArcNode arcNode = vexNode.getArcNode();
            while (arcNode != null) {
                Indegree[arcNode.index]++;
                arcNode = arcNode.next;
            }
        }
        //计算顶点的位置
        int[] Indegree2 = Indegree.clone();
        int initX = 100;
        for(int k=0;k<Indegree2.length;k++){
            int initY = 0;
            List<Integer> temp = new ArrayList<>();
            for(int i=0;i<Indegree2.length;i++){
                if(Indegree2[i]==0){
                    temp.add(i);
                    Indegree2[i] = -1;
                }
            }
            if(temp.size()==0){
                break;
            }
            int interval = 500/(temp.size()+1);
            for(int key : temp){
                vexNodes[key].setX(initX);
                vexNodes[key].setY(initY += interval);
                ArcNode arcNode = vexNodes[key].getArcNode();
                while(arcNode != null){
                    Indegree2[arcNode.index]--;
                    arcNode = arcNode.next;
                }
            }
            initX+=100;
        }
        //将入度为0的顶点入队列
        for(int i=0;i<Indegree.length;i++){
            if(Indegree[i] == 0){
                queue.offer(vexNodes[i]);
            }
        }
        //拓扑排序
        List<GraphVexNode> sort = new ArrayList<>();
        while(!queue.isEmpty()){
            VexNode vexNode = queue.poll();
            counter++;
            GraphVexNode node = new GraphVexNode(vexNode.getTaskName(),vexNode.getX(),vexNode.getY());
            sort.add(node);
            ArcNode arcNode = vexNode.getArcNode();
            while(arcNode != null){
                if(--Indegree[arcNode.index]==0){
                    queue.offer(vexNodes[arcNode.index]);
                }
                arcNode = arcNode.next;
            }
        }
        if(counter!=vexNodes.length){
            return null;
        }
        return sort;
    }
}
