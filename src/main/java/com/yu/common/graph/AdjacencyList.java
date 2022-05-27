package com.yu.common.graph;

import lombok.Data;

@Data
public class AdjacencyList {
    VexNode[] vexNodes;
    int vexnum,arcnum;

    public AdjacencyList(int vexnum,int arcnum){
        vexNodes = new VexNode[vexnum];
        this.vexnum = vexnum;
        this.arcnum = arcnum;
    }

    public void addVexNode(int i, VexNode vexNode) {
        vexNodes[i] = vexNode;
    }

    public void addArcNode(int i,ArcNode arcNode){
        ArcNode node = vexNodes[i].arcNode;
        if(node==null){
            vexNodes[i].arcNode = arcNode;
        }else{
            while(node.next!=null)
                node = node.next;
            node.next = arcNode;
        }
    }

    public boolean isCyclic()
    {
        boolean[] visited = new boolean[vexnum];
        boolean[] recStack = new boolean[vexnum];

        for (int i = 0; i < vexnum; i++)
            if (isCyclicUtil(i, visited, recStack))
                return true;

        return false;
    }

    private boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack)
    {
        if (recStack[i])
            return true;

        if (visited[i])
            return false;

        visited[i] = true;

        recStack[i] = true;

        ArcNode arcNode = vexNodes[i].getArcNode();
        while (arcNode!=null){
            if (isCyclicUtil(arcNode.index, visited, recStack))
                return true;
            arcNode = arcNode.next;
        }
        recStack[i] = false;

        return false;
    }
}
