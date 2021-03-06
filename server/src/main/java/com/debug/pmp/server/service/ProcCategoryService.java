package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.ProcCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawell
 * @since 2021-02-03
 */
public interface ProcCategoryService extends IService<ProcCategoryEntity> {

    PageUtil queryPage(Map<String,Object> params);

    void saveCategory(ProcCategoryEntity categoryEntity);

    void updateCategory(ProcCategoryEntity categoryEntity);

    void deleteCategory(List<String> ids);

    List<Map<String,String>> getAllCategory();
}
