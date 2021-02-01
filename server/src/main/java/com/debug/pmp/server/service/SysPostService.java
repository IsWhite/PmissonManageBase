package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysPostEntity;

import java.util.Map;

/**
 * Created by Administrator on 2019/8/1.
 */
public interface SysPostService extends IService<SysPostEntity>{

    PageUtil queryPage(Map<String,Object> params);

    void savePost(SysPostEntity entity);

    void updatePost(SysPostEntity entity);

    void deletePatch(Long[] ids);
}
