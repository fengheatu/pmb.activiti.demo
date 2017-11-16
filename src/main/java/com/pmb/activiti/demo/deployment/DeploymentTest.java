package com.pmb.activiti.demo.deployment;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DeploymentTest {


    @Test
    public void crateDeploymentBuilderTest() throws FileNotFoundException {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        InputStream is1 = new FileInputStream(new File("E:\\practice_space\\pmb.activiti.demo\\src\\main\\resources\\bpmn\\flow_inputstream1.png"));
        InputStream is2 = new FileInputStream(new File("E:\\practice_space\\pmb.activiti.demo\\src\\main\\resources\\bpmn\\flow_inputstream2.png"));

        deploymentBuilder.addInputStream("inputA",is1);
        deploymentBuilder.addInputStream("inputB",is2);

        deploymentBuilder.name("deploymentBuilder");
        deploymentBuilder.enableDuplicateFiltering();

        deploymentBuilder.deploy();
    }

    @Test
    public void processTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();

        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("bpmn/First.bpmn");
        builder.enableDuplicateFiltering();
        Deployment deployment = builder.deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
                .singleResult();
        System.out.println(definition.getDiagramResourceName());

    }

    @Test
    public void suspendAndActivateProcessTest() {
        ProcessEngine engine  = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/First.bpmn")
                .enableDuplicateFiltering()
                .deploy();

        //查询定义流程实体
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        //终止
        repositoryService.suspendProcessDefinitionById(definition.getId());

        //激活
        repositoryService.activateProcessDefinitionById(definition.getId());
    }


    @Test
    public void candidateTest() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = engine.getRepositoryService();

        IdentityService identityService = engine.getIdentityService();

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/First.bpmn")
                .deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        
        createUser(identityService,"user1","he1","feng1","abc@163.com","123");
        createUser(identityService,"user2","he2","feng2","abc@163.com","123");
        createUser(identityService,"user3","he3","feng3","abc@163.com","123");

        repositoryService.addCandidateStarterUser(definition.getId(),"user1");
        repositoryService.addCandidateStarterUser(definition.getId(),"user2");

    }

    private void createUser(IdentityService identityService, String id, String first, String last, String email, String password) {
        User user = identityService.newUser(id);
        user.setFirstName(first);
        user.setLastName(last);
        user.setEmail(email);
        user.setPassword(password);
        identityService.saveUser(user);
    }


}
