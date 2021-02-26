package com.debug.pmp.server.util;

import com.debug.pmp.server.ActivitiTest;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Administrator
 * @Date 2021/2/24
 * @Description 工作流启动工具
 */
public class ActivitiUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest.class);


    public static ProcessEngine getProcessEngine() {
        // 创建流程引擎，使用内存数据库
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        //设置
        ProcessEngine processEngine = cfg.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate("true")
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/pmp?useSSL=false&serverTimezone=UTC")
                .setJdbcUsername("root")
                .setJdbcPassword("123456")
                .setAsyncExecutorActivate(false)
                .buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;
        LOGGER.info("流程引擎名称【{}】,版本为【{}】", name, version);
        return processEngine;
    }

    public static ProcessDefinition deployment(ProcessEngine processEngine) {
        // 部署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        //此文件只能在resources下。必须是.bpmn20.xml 格式
        deploymentBuilder.addClasspathResource("activiti/applyStorage.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String id = deployment.getId();

        // 验证已部署的流程定义 ，获取流程定义对像
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(id)
                .singleResult();
        String processDefinitionId = processDefinition.getId();
        String processDefinitionName = processDefinition.getName();
        LOGGER.info("流程定义名称为【{}】,id为【{}】",processDefinitionName,processDefinitionId);
        return processDefinition;
    }

    public static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        // 启动流程并返回流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程【{}】",processInstance.getProcessDefinitionKey());
        return processInstance;
    }
}