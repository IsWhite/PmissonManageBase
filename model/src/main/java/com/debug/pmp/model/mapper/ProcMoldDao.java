package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.ProcMoldEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pawell
 * @since 2021-02-18
 */
@Mapper
public interface ProcMoldDao extends BaseMapper<ProcMoldEntity> {

    void removeByMoldIds(@Param("ids") String sqlStr);
}
