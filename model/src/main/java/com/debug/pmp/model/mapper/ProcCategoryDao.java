package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.ProcCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pawell
 * @since 2021-02-03
 */
@Mapper
public interface ProcCategoryDao extends BaseMapper<ProcCategoryEntity> {

    void removeByCategoryIds(@Param("ids")String ids);
}
