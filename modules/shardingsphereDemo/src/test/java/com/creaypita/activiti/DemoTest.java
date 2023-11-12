package com.creaypita.activiti;

import com.creasypita.activiti.DemoApplication;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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

//    @Test
//    public void showservice(){
////        RepositoryService repositoryService = processEngine.getRepositoryService();
//        System.out.println(repositoryService);
//        repositoryService.
//    }
    @Test
    public void getProcessDefinition() {

        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("second_approve.bpmn20.xml")
                .addClasspathResource("process.bpmn20.xml")
                .deploy();

        //部署id
        String deploymentId = deployment.getId();

        //流程对应对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        System.out.println(String.format( "流程名称 [%s],流程ID [%s],流程KEY [%s]",processDefinition.getName(),processDefinition.getId(),processDefinition.getKey()));
//        logger.info("流程名称 [{}],流程ID [{}],流程KEY [{}]",processDefinition.getName(),processDefinition.getId(),processDefinition.getKey());
//        return processDefinition;
    }
}
