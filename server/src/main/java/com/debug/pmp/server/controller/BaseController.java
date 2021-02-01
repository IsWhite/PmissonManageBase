package com.debug.pmp.server.controller;/**
 * Created by Administrator on 2019/7/29.
 */

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/29 14:35
 **/
@Controller
@RequestMapping("/base")
public class BaseController {

    private static final Logger log= LoggerFactory.getLogger(BaseController.class);

    /**
     * 第一个案例-json格式响应体交互
     * @param name
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse info(String name){
        BaseResponse response=new BaseResponse(StatusCode.Success);

        if (StringUtils.isBlank(name)){
            name="权限管理平台!";
        }
        response.setData(name);

        return response;
    }


    /**
     * 第二个案例：页面跳转-塞值
     * @param name
     * @return
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(String name, ModelMap modelMap){
        if (StringUtils.isBlank(name)){
            name="权限管理平台!";
        }
        modelMap.put("name",name);
        modelMap.put("app","权限管理系统");

        return "pageOne";
    }
}































