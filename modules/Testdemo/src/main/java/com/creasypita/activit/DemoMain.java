package com.creasypita.activit;

import org.activiti.engine.impl.db.DbSchemaCreate;
import org.activiti.engine.task.Task;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lujq on 10/28/2023.
 */
public class DemoMain {
    private static  final Logger logger= LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) throws ParseException {
        logger.info("启动程序");

        //创建流程引擎(基于内存数据库的流程引擎对象)
        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        //启动流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);


        //处理流程任务
        processTask(processEngine, processInstance);
        logger.info("结束程序");
    }

    private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        //流程不为空并且流程没有结束
        while (processInstance!=null && !processInstance.isEnded()){
            //任务Service用于管理、查询任务，例如签收、办理、指派等
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            for (Task task:list) {
                logger.info("待处理任务 [{}]",task.getName());
                //提交
                taskService.complete(task.getId(),null);
                //获取流程对象最新状态
                processInstance=processEngine.getRuntimeService().createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId()).singleResult();
            }
            logger.info("待处理任务数量 [{}]",list.size());
        }
    }

    private static void processTask1(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        //流程不为空并且流程没有结束
        while (processInstance!=null && !processInstance.isEnded()){
            //任务Service用于管理、查询任务，例如签收、办理、指派等
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            for (Task task:list) {
                logger.info("待处理任务 [{}]",task.getName());
                Map<String, Object> variables = getStringObjectMap(processEngine, scanner, task);
                //提交
                taskService.complete(task.getId(),variables);
                //获取流程对象最新状态
                processInstance=processEngine.getRuntimeService().createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId()).singleResult();


            }
            logger.info("待处理任务数量 [{}]",list.size());
        }
        scanner.close();
    }

    private static Map<String, Object> getStringObjectMap(ProcessEngine processEngine, Scanner scanner, Task task) throws ParseException {
        //表单Service用于读取和流程、任务相关的表单数据
        FormService formService = processEngine.getFormService();
        //获取任务表单
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        //获取属性集合
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        //存储要提交的表单
        Map<String, Object> variables=new HashMap<String, Object>();
        String line=null;
        for (FormProperty property:formProperties){
            //如果是string类型
            if(StringFormType.class.isInstance(property.getType())){
                logger.info("请输入 {}",property.getName());
                line=scanner.nextLine();
                logger.info("您输入的内容是 [{}]",line);
                variables.put(property.getId(),line);
            }else if(DateFormType.class.isInstance(property.getType())){
                logger.info("请输入 {} 格式 (yyyy-MM-dd)",property.getName());
                line=scanner.nextLine();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(line);
                variables.put(property.getId(),date);
            }else{
                logger.info("类型不支持");
            }
        }
        return variables;
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        //运行时Service可以处理所有运行状态的流程实例流程控制(开始,暂停,挂起等)
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        logger.info("启动流程[{}]",processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        //流程仓库Service,可以管理流程仓库例如部署删除读取流程资源
        RepositoryService repositoryService = processEngine.getRepositoryService();
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

        logger.info("流程名称 [{}],流程ID [{}],流程KEY [{}]",processDefinition.getName(),processDefinition.getId(),processDefinition.getKey());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
//        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        //构造流程引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;
        logger.info("流程引擎名称 [{}],版本 [{}]",name,version);
        return processEngine;
    }
}
