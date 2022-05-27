package com.yu.common.graph;

import lombok.Data;

@Data
public class VexNode {
    String TaskName;
    int x;
    int y;
    ArcNode arcNode;

    public VexNode(){

    }
    public VexNode(String taskName){
        this.TaskName = taskName;
    }
}
