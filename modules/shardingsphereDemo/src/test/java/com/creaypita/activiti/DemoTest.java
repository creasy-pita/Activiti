package com.creaypita.activiti;

import com.creasypita.activiti.DemoApplication;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lujq on 11/12/2023.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoTest {

    @Autowired
//    public ProcessEngine processEngine;
    public RepositoryService repositoryService;

    @Test
    public void showservice(){
//        RepositoryService repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
    }
}
