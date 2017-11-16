package com.pmb.activiti.demo.group;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GroupTest {


    @Test
    public void saveGroup() {
        ProcessEngine engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        Group group = identityService.newGroup("1");
        group.setName("技术部");
        group.setType("java");
        identityService.saveGroup(group);


    }

    @Test
    public void updateGroup() {
        ProcessEngine engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        Group data = identityService.createGroupQuery().groupId("1").singleResult();
        data.setName("java开发部门");
        identityService.saveGroup(data);
    }

    @Test
    public void queryList() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Group> datas = identityService.createGroupQuery().orderByGroupId().asc().list();
        for(Group group : datas) {
            System.out.println(group.getId());
        }
    }
}
