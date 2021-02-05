package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.ProcCategoryEntity;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.ProcCategoryService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pawell
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/proc/procCategory")
public class ProcCategoryController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(ProcCategoryController.class);
    @Autowired
    private ProcCategoryService procCategoryService;


    //分页列表模糊查询
    @RequestMapping("/list")
    @RequiresPermissions("proc:procCategory:list")
    public BaseResponse list(@RequestParam Map<String, Object> paramMap) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String, Object> resMap = Maps.newHashMap();
        try {
            PageUtil page = procCategoryService.queryPage(paramMap);
            resMap.put("page", page);

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    @LogAnnotation("新增商品类别")
    @RequestMapping(value = "/save")
    @RequiresPermissions("proc:procCategory:save")
    public BaseResponse save(@RequestBody @Validated ProcCategoryEntity categoryEntity, BindingResult result) {
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)) {
            return new BaseResponse(StatusCode.Fail.getCode(), res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("表单提交实体类{}", categoryEntity);
            procCategoryService.saveCategory(categoryEntity);

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }

    //详情

    @RequestMapping(value = "/info/{id}")
    public BaseResponse info(@PathVariable String id){
        log.info(id);
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            ProcCategoryEntity procCategoryEntity = procCategoryService.getById(id);
            Map<String ,Object> resMaps = Maps.newHashMap();
            resMaps.put("category",procCategoryEntity);
            response.setData(resMaps);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }

}

