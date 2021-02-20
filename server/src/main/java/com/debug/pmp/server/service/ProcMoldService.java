package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.ProcMoldEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawell
 * @since 2021-02-18
 */
public interface ProcMoldService extends IService<ProcMoldEntity> {

    PageUtil queryPage(Map<String, Object> paramMap);

    void saveProMold(ProcMoldEntity procMoldEntity);

    ProcMoldEntity getMoldInfo(String id);

    void updateMold(ProcMoldEntity procMoldEntity);

    void deleteMold(List<String> ids);
}
