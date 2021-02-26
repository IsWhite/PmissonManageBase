package com.debug.pmp.server.service;

import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.ProcStorageEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
public interface ProcStorageService extends IService<ProcStorageEntity> {

    PageUtil queryPage(Map<String, Object> paramMap);

    void saveStorage(ProcStorageEntity procStorageEntity);
}
