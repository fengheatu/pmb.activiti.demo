package com.pmb.activiti.demo.job;

import com.mchange.v2.resourcepool.ResourcePool;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.runtime.Job;
import org.junit.Test;

/**
 * @author: he.feng
 * @date: 17:07 2017/10/30
 * @desc:
 **/
public class JobExceptionDelegateTest {

    @Test
    public void jobExceptionDelegateTest() throws InterruptedException {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        engine.getProcessEngineConfiguration().getJobExecutor().start();

        RepositoryService repositoryService = engine.getRepositoryService();

        repositoryService.createDeployment().addClasspathResource("bpmn/jobException.bpmn").deploy();

        Thread.sleep(2000);

        engine.getProcessEngineConfiguration().getJobExecutor().shutdown();

        ManagementService managementService = engine.getManagementService();

        Job job = managementService.createJobQuery().singleResult();

        String msg = managementService.getJobExceptionStacktrace(job.getId());

        System.out.println(msg);
    }
}
