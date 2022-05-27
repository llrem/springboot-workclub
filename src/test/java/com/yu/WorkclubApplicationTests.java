package com.yu;

import com.yu.common.graph.AdjacencyList;
import com.yu.common.graph.ArcNode;
import com.yu.common.graph.VexNode;
import com.yu.common.hashing.Hash;
import com.yu.controller.StatisticController;
import com.yu.entity.TmTaskDependence;
import com.yu.service.PmProjectService;
import com.yu.service.TmTaskService;
import javafx.scene.shape.Arc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class WorkclubApplicationTests {
    @Autowired
    PmProjectService projectService;
    @Autowired
    TmTaskService taskService;

    @Test
    public void test(){

        StatisticController  controller = new StatisticController();
        controller.getTopology("6256502");
        System.out.println("");
    }
}
