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
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * <p>
 * 前端控制器
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

    @Autowired
    private TaskService taskService;

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
    @RequestMapping(value = "/getUsersInfomation")
    @RequiresPermissions("proc:procStorageApply:getUser")
    public BaseResponse getUsersInfomation(){
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
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            ActivitiUtil activitiUtil = new ActivitiUtil();
            //创建流程引擎
            ProcessEngine processEngine = activitiUtil.getProcessEngine();
            //部署流程定义文件
            ProcessDefinition processDefinition = activitiUtil.deployment(processEngine);
            //启动运行流程
             activitiUtil.getProcessInstance(processEngine, processDefinition);
            Map<String, Object> variables = new HashMap<>();
            variables.put("applyUser", "employee1"); //申请人名称 --- 对应表 act_ru_variable的，_NAME ,_TEXT字段
            variables.put("days", 3); //请假天数
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", variables);
            //assertNotNull(processInstance);
            System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}

