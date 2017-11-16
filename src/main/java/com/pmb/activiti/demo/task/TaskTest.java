package com.pmb.activiti.demo.task;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: he.feng
 * @Date: 13:58 2017/10/27
 * @Desc:
 **/
public class TaskTest {

    @Test
    public void taskTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = engine.getTaskService();

        Task task1 = taskService.newTask();
        taskService.saveTask(task1);

        Task task2 = taskService.newTask("审核任务");
        taskService.saveTask(task2);
     }

     @Test
    public void basicVariableTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = engine.getTaskService();

        Task task = taskService.newTask("task1");
        taskService.saveTask(task);

        Date date = new Date();
        short s = 3;

        taskService.setVariable(task.getId(),"arg0",false);
        taskService.setVariable(task.getId(),"arg1",date);
        taskService.setVariable(task.getId(),"arg2",1.5D);
        taskService.setVariable(task.getId(),"arg3",2);
        taskService.setVariable(task.getId(),"arg4",10L);
        taskService.setVariable(task.getId(),"arg5",null);
        taskService.setVariable(task.getId(),"arg6",s);
        taskService.setVariable(task.getId(),"arg7","test");
     }


     @Test
    public void localVariableTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = engine.getTaskService();

         RuntimeService runtimeService = engine.getRuntimeService();

         RepositoryService repositoryService  = engine.getRepositoryService();

         Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/First.bpmn")
                 .deploy();

         ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                 .deploymentId(deployment.getId())
                 .singleResult();

         ProcessInstance processInstance = runtimeService.startProcessInstanceById(definition.getId());

         Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                 .singleResult();
         taskService.setVariable(task.getId(),"days",10);
         taskService.setVariableLocal(task.getId(),"target","欧洲");

         System.out.println("获取休假天数: " + taskService.getVariable(task.getId(),"days"));
         System.out.println("获取目的地： " + taskService.getVariable(task.getId(),"target"));
         System.out.println("local：" + taskService.getVariableLocal(task.getId(),"days"));

     }


     @Test
    public void attachmentTest() throws FileNotFoundException {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = engine.getTaskService();

        RuntimeService runtimeService = engine.getRuntimeService();

        RepositoryService repositoryService = engine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/First.bpmn")
                .deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());

        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();

        Attachment attachment1 = taskService.createAttachment("web url",task.getId(),instance.getId(),"163.com","163 web page","http://www.163.com");

        InputStream is = new FileInputStream(new File("E:\\practice_space\\pmb.activiti.demo\\src\\main\\resources\\bpmn\\classpath.png"));
         Attachment attachment2 = taskService.createAttachment("web url",task.getId(),instance.getId(),"163.com","163 web page",is);

         List<Attachment> attachmentList = taskService.getTaskAttachments(task.getId());
         System.out.println("流程附件数量：" + attachmentList.size());


         List<Attachment> attachmentList2 = taskService.getProcessInstanceAttachments(instance.getId());
         System.out.println("流程附件数量：" + attachmentList2.size());


        Attachment attachment3 = taskService.getAttachment(attachment1.getId());
         System.out.println("流程附件数量：" + attachment3);

     }

     @Test
    public void completeTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService =engine.getTaskService();

        RuntimeService runtimeService = engine.getRuntimeService();

        RepositoryService repositoryService = engine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/vacation2.bpmn")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        Task  task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                .singleResult();

        Map<String,Object> vars = new HashMap<String,Object>();
        vars.put("days",2);
        taskService.complete(task.getId(),vars);

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Integer days = (Integer) taskService.getVariable(task.getId(),"days");

        if(days > 5) {
            System.out.println("大于五天，不批");
        }else {
            System.out.println("小于五天，完成任务，流程结束");
            taskService.complete(task.getId());
        }

     }



     @Test
     public void candidateTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();

        RuntimeService runtimeService = engine.getRuntimeService();

        TaskService taskService = engine.getTaskService();

        repositoryService.createDeployment().addClasspathResource("bpmn/TaskListener.bpmn").deploy();

        runtimeService.startProcessInstanceByKey("process1");

        List<Task> tasks = taskService.createTaskQuery().taskAssignee("user1").list();
         System.out.println("分配到Angus用户的任务数量：" + tasks.size());
     }

}
