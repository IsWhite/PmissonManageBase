package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.ProcStorageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
public interface ProcStorageDao extends BaseMapper<ProcStorageEntity> {

    void deleteByIds(String sqlIds);
}
