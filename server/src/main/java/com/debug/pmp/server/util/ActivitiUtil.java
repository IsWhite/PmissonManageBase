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
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    public static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition, Map<String, Object> variables) {
        // 启动流程并返回流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),variables);
        LOGGER.info("启动流程【{}】",processInstance.getProcessDefinitionKey());
        return processInstance;
    }


    /***************************生成流程图片******************************/
    public static  void  flowPicture( ProcessEngine processEngine,String deploymentId) throws IOException {
        ProcessInstance process = processEngine.getRuntimeService()// 表示正在执行的流程实例和执行对象
                .createProcessInstanceQuery()// 创建流程实例查询
                .processInstanceId("流程实例ID")// 使用流程实例ID查询
                .singleResult();
        if (process == null) {
            System.out.println("流程已经结束");
        } else {
            System.out.println("流程没有结束");
        }
        /** 将生成图片放到文件夹下 */
        //String deploymentId = "50001";// 流程部署ID     就是部署对象ID
        // 获取图片资源名称
        List<String> listDown = processEngine.getRepositoryService()//
                .getDeploymentResourceNames(deploymentId);// 流程部署ID
        // 定义图片资源的名称
        String resourceName = "图片资源的名称";
        if (listDown != null && listDown.size() > 0) {
            for (String name : listDown) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }
        // 获取图片的输入流
        InputStream in = processEngine.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);
        // 将图片生成到D盘的目录下
        File file = new File("C:/Users/Administrator/Desktop/新建文件夹diagrams/" + resourceName);
        // 将输入流的图片写到D盘下
        FileUtils.copyInputStreamToFile(in, file);
        /*****************************生成流程图片*********************************************************/
    }
}