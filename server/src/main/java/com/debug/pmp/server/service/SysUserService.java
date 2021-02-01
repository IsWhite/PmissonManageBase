package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysUserEntity;

import java.util.Map;

/**
 * Created by Administrator on 2019/7/31.
 */
public interface SysUserService extends IService<SysUserEntity>{

    //修改密码
    boolean updatePassword(Long userId,String oldPassword,String newPassword);

    PageUtil queryPage(Map<String,Object> map);

    void saveUser(SysUserEntity entity);

    SysUserEntity getInfo(Long userId);

    void updateUser(SysUserEntity entity);

    void deleteUser(Long[] ids);

    void updatePsd(Long[] ids);
}
































