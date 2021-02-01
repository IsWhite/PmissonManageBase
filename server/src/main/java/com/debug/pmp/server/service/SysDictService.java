package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysDictEntity;

import java.util.Map;

//数据字典
public interface SysDictService extends IService<SysDictEntity> {

    PageUtil queryPage(Map<String, Object> params);
}

