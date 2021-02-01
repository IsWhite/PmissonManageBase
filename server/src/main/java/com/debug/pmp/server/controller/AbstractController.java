package com.debug.pmp.server.controller;/**
 * Created by Administrator on 2019/7/30.
 */

import com.debug.pmp.model.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/30 12:23
 **/
@Controller
public abstract class AbstractController {

    //日志
    protected Logger log= LoggerFactory.getLogger(getClass());

    //获取当前登录用户的详情
    protected SysUserEntity getUser(){
        SysUserEntity user= (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    protected Long getUserId(){
        return getUser().getUserId();
    }

    protected String getUserName(){
        return getUser().getUsername();
    }

    protected Long getDeptId(){
        return getUser().getDeptId();
    }
}