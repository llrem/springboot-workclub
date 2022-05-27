package com.yu.dto;

import com.yu.common.graph.GraphArcNode;
import com.yu.common.graph.GraphVexNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TopologyGraphParam {
    List<GraphVexNode> graphVexNodes;
    List<GraphArcNode> graphArcNodes;
}
