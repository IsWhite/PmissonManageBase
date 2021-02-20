package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.ProcMoldEntity;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.ProcCategoryService;
import com.debug.pmp.server.service.ProcMoldService;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pawell
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/proc/procMold")
public class ProcMoldController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(ProcMoldController.class);

    @Autowired
    private ProcMoldService procMoldService;

    @Autowired
    private ProcCategoryService procCategoryService;

    //分页列表模糊查询
    @RequestMapping("/list")
    @RequiresPermissions("proc:procMold:list")
   // @RequiresPermissions("proc:procMold:list")
    public BaseResponse list(@RequestParam Map<String, Object> paramMap) {
        log.info("查询提交的数据{}", paramMap);
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String, Object> resMap = Maps.newHashMap();
        try {
            PageUtil page = procMoldService.queryPage(paramMap);
            log.info("列表数据{}", page);
            resMap.put("page", page);

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    @LogAnnotation("新增商品类型")
    @RequestMapping(value = "/save")
    @RequiresPermissions("proc:procMold:save")
    public BaseResponse save(@RequestBody @Validated ProcMoldEntity procMoldEntity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)) {
            return new BaseResponse(StatusCode.Fail.getCode(), res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("表单提交实体类{}", procMoldEntity);

            procMoldService.saveProMold(procMoldEntity);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "/info/{id}")
    @RequiresPermissions("proc:procMold:info")
    public BaseResponse getInfo(@PathVariable String id){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("详情id{}", id);
          ProcMoldEntity moldEntity =  procMoldService.getMoldInfo(id);
          Map<String ,Object> resMap = Maps.newHashMap();
          resMap.put("mold",moldEntity);
          response.setData(resMap);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    @LogAnnotation("修改商品类型")
    @RequestMapping(value = "/update")
    @RequiresPermissions("proc:procMold:update")
    public BaseResponse update(@RequestBody @Validated ProcMoldEntity procMoldEntity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)) {
            return new BaseResponse(StatusCode.Fail.getCode(), res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("修改提交的实体类{}", procMoldEntity);
            procMoldService.updateMold(procMoldEntity);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    //获得商品类别下拉选的值
    @RequestMapping(value = "/getCategory")
    public BaseResponse getCategory(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            List<Map<String,String>> allCategory = procCategoryService.getAllCategory();
            response.setData(allCategory);

        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //删除
    @LogAnnotation("删除商品类型")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("proc:procMold:delete")
    public BaseResponse delete(@RequestBody List<String> ids){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{

            procMoldService.deleteMold(ids);

        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}

