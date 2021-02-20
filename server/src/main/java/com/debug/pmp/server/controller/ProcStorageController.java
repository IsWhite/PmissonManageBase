package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.server.service.ProcStorageService;
import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
@RestController
@RequestMapping("/proc/procStorage")
public class ProcStorageController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(ProcStorageController.class);

    @Autowired
    private ProcStorageService storageService;

    @RequestMapping(value = "/applyList")
    public BaseResponse appliList(@RequestParam Map<String, Object> paramMap){
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


}

