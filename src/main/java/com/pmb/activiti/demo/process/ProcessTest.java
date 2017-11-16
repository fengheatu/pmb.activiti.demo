package com.pmb.activiti.demo.process;

import org.activiti.engine.*;
import org.junit.Test;

/**
 * @Author: he.feng
 * @Date: 13:31 2017/10/30
 * @Desc:
 **/
public class ProcessTest {


    @Test
    public void signaleEventReceivedTest() {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();

        RuntimeService runtimeService = engine.getRuntimeService();




    }

}
