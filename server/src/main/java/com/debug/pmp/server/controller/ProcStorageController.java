package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.ProcStorageEntity;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.ProcStorageService;

import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.util.ActivitiUtil;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.io.File;
import java.io.InputStream;
import java.util.*;


/**
 * <p>
 * 前端控制器  仓库管理
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
@RestController
@RequestMapping("/proc/procStorage")
public class ProcStorageController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(ProcStorageController.class);

    @Autowired
    private ProcStorageService storageService;
    @Autowired
    private SysUserService userService;

    /************工作流**************/
    @Autowired
    private RuntimeService runtimeService;

    //@Autowired
    //private TaskService taskService;

    /************工作流**************/
    //仓库申请列表
    @RequestMapping(value = "/applyList")
    @RequiresPermissions("proc:procStorageApply:list")
    public BaseResponse appliList(@RequestParam Map<String, Object> paramMap) {
        log.info("查询提交的数据{}", paramMap);
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String, Object> resMap = Maps.newHashMap();
        try {
            PageUtil page = storageService.queryPage(paramMap);
            log.info("列表数据{}", page);
            resMap.put("page", page);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        response.setData(resMap);
        return response;
    }


    //保存
    @LogAnnotation("保存仓库")
    @RequestMapping(value = "/save")
    @RequiresPermissions("proc:procStorageApply:save")
    public BaseResponse save(@RequestBody @Validated ProcStorageEntity procStorageEntity, BindingResult bindingResult){
        String res = ValidatorUtil.checkResult(bindingResult);
        if (StringUtils.isNotBlank(res)) {
            return new BaseResponse(StatusCode.Fail.getCode(), res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        log.info("保存仓库{}",bindingResult);
        try {
         log.info("保存仓库提交的表单{}",procStorageEntity);
         storageService.saveStorage(procStorageEntity);
        }catch (Exception e){
            response  = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获得所有用户的 id ,姓名， 和手机号(下拉选-用户)
    @RequestMapping(value = "/getUsersInfo",method = RequestMethod.GET)
    @RequiresPermissions("proc:procStorageApply:getUser")
    public BaseResponse getUsersInfo(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            List<Map<String,String>> userNamePhone = userService.getUserNamePhone();
            response.setData(userNamePhone);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //表单详情
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    @RequiresPermissions("proc:procStorageApply:info ")
    public BaseResponse getInfo(@PathVariable String  id){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("id为{}",id);
            ProcStorageEntity entity = storageService.getInfoById(id);
            Map<String ,Object> reMaps = Maps.newHashMap();
            reMaps.put("storage",entity);
            response.setData(reMaps);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //修改
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("proc:procStorageApply:update")
    public BaseResponse update(@RequestBody @Validated ProcStorageEntity storageEntity){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("修改提交的表单{}",storageEntity);
            storageService.updateStorage(storageEntity);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //删除
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("proc:procStorageApply:delete")
    public BaseResponse delete(@RequestBody @Validated List<String> ids){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("删除的ids{}" ,ids);
            storageService.deleteByIds(ids);
        }catch (Exception e){
            response  =  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }




    //申请仓库
    @RequestMapping(value = "/apply")
    public BaseResponse apply() {
        log.info("申请{}", "申请");
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUser", "employee1"); //申请人名称 --- 对应表 act_ru_variable的，_NAME ,_TEXT字段
        variables.put("days", 3); //请假天数

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            ActivitiUtil activitiUtil = new ActivitiUtil();
            //创建流程引擎
            ProcessEngine processEngine = activitiUtil.getProcessEngine();
            //部署流程定义文件
            ProcessDefinition processDefinition = activitiUtil.deployment(processEngine);
            //启动运行流程
             activitiUtil.getProcessInstance(processEngine, processDefinition,variables);
            //通过Key启动工作流
           // ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", variables);
//            System.out.println("流程实例ID" + processInstance.getId() + ", 流程定义ID" + processInstance.getProcessDefinitionId());
          /*********************************************************************************************************************/
            List<ProcessDefinition> list = processEngine.getRepositoryService()//
                    .createProcessDefinitionQuery()//
                    .orderByProcessDefinitionVersion().asc()// 使用流程定义的版本升序排列
                    .list();
            /**
             * Map<String,ProcessDefinition> map集合的key：流程定义的key map集合的value：流程定义的对象
             * map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
             */
            Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
            if (list != null && list.size() > 0) {
                for (ProcessDefinition pd : list) {
                    map.put(pd.getKey(), pd);
                }
            }
            List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
            if (pdList != null && pdList.size() > 0) {
                for (ProcessDefinition pd : pdList) {
                    System.out.println("流程定义ID:" + pd.getId());// 流程定义的key+版本+随机生成数
                    System.out.println("流程定义的名称:" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
                    System.out.println("流程定义的key:" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
                    System.out.println("流程定义的版本:" + pd.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
                    System.out.println("资源名称bpmn文件:" + pd.getResourceName());
                    System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
                    System.out.println("部署对象ID：" + pd.getDeploymentId());
                    System.out.println("#########################################################");
                }
            }
         /*********************************************************************************************************************/
            System.out.println("这个是部署对象ID：" +processDefinition.getDeploymentId());
            ProcessInstance process = processEngine.getRuntimeService()// 表示正在执行的流程实例和执行对象
                    .createProcessInstanceQuery()// 创建流程实例查询
                    .processInstanceId(processDefinition.getDeploymentId())// 使用流程实例ID查询
                    .singleResult();
            if (process == null) {
                System.out.println("流程已经结束");
            } else {
                System.out.println("流程没有结束");
            }
            /*********************************************************************************************************************/
            /** 将生成图片放到文件夹下 */
           //ActivitiUtil.flowPicture(processEngine,"65001"); //好用
            /*********************************************************************************************************************/

            /*********************************************************************************************************************/


        } catch (Exception e) {

            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}

