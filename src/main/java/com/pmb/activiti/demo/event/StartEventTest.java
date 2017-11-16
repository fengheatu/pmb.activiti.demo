package com.pmb.activiti.demo.event;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.List;

/**
 * @author: he.feng
 * @date: 10:59 2017/10/31
 * @desc:
 **/
public class StartEventTest {

    @Test
    public void startEventTest() throws InterruptedException {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        engine.getProcessEngineConfiguration().getJobExecutor().start();

        RepositoryService repositoryService = engine.getRepositoryService();

        RuntimeService runtimeService = engine.getRuntimeService();

        repositoryService.createDeployment().addClasspathResource("bpmn/TimerStartEvent.bpmn").deploy();

        Thread.sleep(1000 * 60);

        engine.getProcessEngineConfiguration().getJobExecutor().shutdown();

        List<ProcessInstance> ints = runtimeService.createProcessInstanceQuery().list();
        System.out.println(ints.size());

    }
}
