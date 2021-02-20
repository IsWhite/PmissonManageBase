package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.ProcCategoryEntity;
import com.debug.pmp.model.entity.ProcMoldEntity;
import com.debug.pmp.model.entity.ProcStorageEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.ProcStorageDao;
import com.debug.pmp.server.service.ProcStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
@Service("ProcStorageService")
public class ProcStorageServiceImpl extends ServiceImpl<ProcStorageDao, ProcStorageEntity> implements ProcStorageService {

    @Override
    public PageUtil queryPage(Map<String, Object> paramMap) {
        String search = (paramMap.get("search") == null) ? "" : paramMap.get("search").toString();

        //调用自封装的分页查询工具
        IPage<ProcStorageEntity> queryPage = new QueryUtil<ProcStorageEntity>().getQueryPage(paramMap);

        QueryWrapper wrapper = new QueryWrapper<ProcStorageEntity>()
                .like(StringUtils.isNotBlank(search), "storage_name", search.trim())
                .or(StringUtils.isNotBlank(search))
                .like(StringUtils.isNotBlank(search), "storage_code", search.trim())
                .orderByDesc("create_time");

        IPage<ProcStorageEntity> resPage = this.page(queryPage, wrapper);
        //获取用户所属的部门、用户的岗位信息
        SysUserEntity user;
        ProcCategoryEntity category;
        for (ProcStorageEntity moldEntity : resPage.getRecords()) {
            try {
//                user = sysUserService.getById(moldEntity.getCreaterId());
//                category = procCategoryService.getById(moldEntity.getCategoryId());
//                moldEntity.setCreaterName(user.getName());
//                moldEntity.setCategoryName(category.getCategoryName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new PageUtil(resPage);
    }
}
