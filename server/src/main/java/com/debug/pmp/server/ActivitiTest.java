package com.debug.pmp.server;

/**
 * @Author Administrator
 * @Date 2021/2/23
 * @Description
 */

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.text.SimpleDateFormat;

/**
 * 开启流程,根据流程定义key开启流程,
 * 如果流程定义表中有多个相同的流程定义key,activiti会根据版本号字段VERSION_选择最新的版本来开启流程
 */



public class ActivitiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest.class);


    public static void main(String[] args) throws ParseException {
        LOGGER.info("程序启动成功");
        //创建流程引擎
        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        ProcessDefinition processDefinition = deployment(processEngine);

        //启动运行流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //处理流程任务
        processTask(processEngine, processInstance);

        LOGGER.info("程序结束");
    }

    private static ProcessEngine getProcessEngine() {
        //获取ProcessEngine对象
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate("true")
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/pmp?useSSL=false&serverTimezone=UTC")
                .setJdbcUsername("root")
                .setJdbcPassword("123456")
                .setAsyncExecutorActivate(false)
                .buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;
        LOGGER.info("流程引擎名称【{}】,版本为【{}】",name,version);
        return processEngine;
    }

    private static ProcessDefinition deployment(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("activiti/applyStorage.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String id = deployment.getId();

        //获取流程定义对像
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(id)
                .singleResult();
        String processDefinitionId = processDefinition.getId();
        String processDefinitionName = processDefinition.getName();
        LOGGER.info("流程定义名称为【{}】,id为【{}】",processDefinitionName,processDefinitionId);
        return processDefinition;
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程【{}】",processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    //全部办结
    private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()){
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            LOGGER.info("待处理任务数为【{}】",list.size());
            for (Task task:list) {
                LOGGER.info("待处理的任务【{}】",task.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();
                Map<String, Object> variables = getStringObjectMap(scanner, formProperties);
                //提交表单
                taskService.complete(task.getId(),variables);
                //提交后更新流程实例
                processInstance = processEngine.getRuntimeService()
                        .createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();
            }
        }
        scanner.close();
    }

    private static Map<String, Object> getStringObjectMap(Scanner scanner, List<FormProperty> formProperties) throws ParseException {
        Map<String,Object> variables = Maps.newHashMap();
        String line = null;
        for (FormProperty property : formProperties){
            //判断用户输入类型
            if(StringFormType.class.isInstance(property.getType())){
                LOGGER.info("请输入【{}】?",property.getName());
                line = scanner.nextLine();
                LOGGER.info("您输入的内容是【{}】",line);
                variables.put(property.getId(),line);
            }else{
                LOGGER.info("请输入时间，格式为yyyy-MM-dd【{}】?",property.getName());
                line = scanner.nextLine();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = sdf.parse(line);
                LOGGER.info("您输入的内容是【{}】",line);
                variables.put(property.getId(),parse);
            }
        }
        return variables;
    }







}
