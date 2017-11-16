package com.pmb.activiti.demo.test;


import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiTest {

    @Test
    public void createTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();
        System.out.println("processEngine:" + processEngine);

    }

    @Test
    public void firstTest() {
        //创建流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        //得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();

        //得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();

        //获取流程任务组件
        TaskService taskService = engine.getTaskService();

        //部署流程文件
        repositoryService.createDeployment().addClasspathResource("bpmn/First.bpmn").deploy();

        //启动流程
        runtimeService.startProcessInstanceByKey("process1");

        //查询第一个任务
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("第一个任务完成前。当前任务名称：" + task.getName());

        //完成第一个任务
        taskService.complete(task.getId());

        //查询第二个任务
        task = taskService.createTaskQuery().singleResult();
        System.out.println("第二个任务完成前。当前任务名称：" + task.getName());

        //完成第二个任务(流程结束)
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().singleResult();
        System.out.println("流程结束后，查找任务：" + task);

    }

    @Test
    public void defaultResourceTest() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine engine = configuration.buildProcessEngine();
        System.out.println("engine:" + engine);

    }


    @Test
    public void createEngine1() {
        /**
         * 1. 通过 ProcessEngineConfiguration 建立流程引擎
         */
        ProcessEngineConfiguration engineConfiguration1 = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        engineConfiguration1.setJdbcDriver("om.mysql.jdbc.Driver");
        engineConfiguration1.setJdbcUrl("jdbc:mysql://localhost:3306/activiti");
        engineConfiguration1.setJdbcUsername("root");
        engineConfiguration1.setJdbcPassword("root");
        engineConfiguration1.setDatabaseSchemaUpdate("true");
        ProcessEngine processEngine1 = engineConfiguration1.buildProcessEngine();
        System.out.println("流程引擎创建成功！");
    }

    @Test
    public void createEngine2() {
        /**
         * 2. 通过加载activiti.cfg.xml文件，获取流程引擎，自动创建数据库及表
         */
        ProcessEngineConfiguration engineConfiguration2 = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine2 = engineConfiguration2.buildProcessEngine();
        System.out.println("使用配置文件创建流程引擎");
    }

    @Test
    public void createEngine3() {
        /**
         * 3. 通过 ProcessEngines 来获取默认的流程引擎
         */
        // 默认会加载classpath径下的 activiti.cfg.xml 文件
        ProcessEngine processEngine3 = ProcessEngines.getDefaultProcessEngine();
        System.out.println("通过 ProcessEngines 来获取流程引擎");
    }


    @Test
    public void createServiceTest() {

        /**创建流程引擎 */
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();
        IdentityService identityService = engine.getIdentityService();
        RuntimeService runtimeService = engine.getRuntimeService();
        TaskService taskService = engine.getTaskService();
        FormService formService = engine.getFormService();
        HistoryService historyService = engine.getHistoryService();
        ManagementService managementService = engine.getManagementService();

    }

    /** 发布流程*/
    @Test
    public void deploy() {
        //获取流程引擎 默认加载resources目录下的activiti.cfg.xml配置文件
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        //获取创库服务Service
        RepositoryService repositoryService = engine.getRepositoryService();
        //部署流程定义
        Deployment deployment = repositoryService.createDeployment()
                //流程图，给计算机看的
                //流程部署的资源有多重类型，bpmn，bpmn20.xml，InputStrean,字符串，zip格式压缩包
                // TODO 第一种方法
                .addClasspathResource("bpmn/helloworld.bpmn")
                .addClasspathResource("bpmn/helloworld.png")
                .deploy();

        System.out.println(deployment.getId() + "   " + deployment.getName());

    }

    /** 启动流程实例*/
    @Test
    public void startProcess() {
        //获取流程引擎 默认加载resources目录下的activiti.cfg.xml配置文件
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        //获取运行时service
        RuntimeService runtimeService = engine.getRuntimeService();
        //启动流程,获取流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("helloworld");
        System.out.println("pid" + pi.getId() + "activitiId:" + pi.getActivityId());
    }


    /** 查看任务 */
    @Test
    public void queryMyTask() {
        //获取流程引擎 默认加载resources目录下的activiti.cfg.xml配置文件
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //指定任务办理着
        String assignee = "张三";
        //获取任务service
        TaskService taskService = engine.getTaskService();
        //查询任务
        List<Task> taskList = taskService
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定任务办理人
                .list();
        //遍历
        for(Task task : taskList) {
            System.out.println("taskId: " + task.getId() + "   taskName: " + task.getName() );
        }

    }

    @Test
    public void completeTask() {
        //获取流程引擎 默认加载resources目录下的activiti.cfg.xml配置文件
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //指定任务办理着
        String taskId = "5002";
        //获取任务service
        TaskService taskService = engine.getTaskService();
        taskService.complete(taskId);
        System.out.println("完成任务");
    }



}
