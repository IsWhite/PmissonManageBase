package com.debug.pmp.server.exception;/**
 * Created by Administrator on 2019/8/4.
 */

import com.debug.pmp.common.exception.CommonException;
import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用异常处理器
 * @Author:debug (SteadyJack)
 * @Date: 2019/8/4 22:07
 **/
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(CommonExceptionHandler.class);

    //处理 访问 没有经过授权的 异常
    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse handleAuthorizationException(AuthorizationException e){
        log.info("访问了没有经过授权的操作或者资源：",e.fillInStackTrace());

        return new BaseResponse(StatusCode.CurrUserHasNotPermission);
    }

    //自定义异常
    @ExceptionHandler(CommonException.class)
    public BaseResponse handleRRException(CommonException e){
        BaseResponse response=new BaseResponse(e.getCode(),e.getMessage());
        return response;
    }

    //数据库出现重复数据记录的异常
    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResponse handleDuplicateKeyException(DuplicateKeyException e){
        BaseResponse response=new BaseResponse(StatusCode.UnknownError);

        log.error(e.getMessage(), e);
        response.setMsg("数据库中已存在该记录!");

        return response;
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e){
        BaseResponse response=new BaseResponse(StatusCode.UnknownError);
        log.error(e.getMessage(), e);
        return response;

    }
}










































