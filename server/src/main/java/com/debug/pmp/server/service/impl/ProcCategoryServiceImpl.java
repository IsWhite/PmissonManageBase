package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.ProcCategoryEntity;
import com.debug.pmp.model.entity.SysDeptEntity;
import com.debug.pmp.model.entity.SysPostEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.ProcCategoryDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.server.service.ProcCategoryService;
import com.debug.pmp.server.service.SysDeptService;
import com.debug.pmp.server.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pawell
 * @since 2021-02-03
 */
@Service("ProcCategoryService")
public class ProcCategoryServiceImpl extends ServiceImpl<ProcCategoryDao, ProcCategoryEntity> implements ProcCategoryService {

    private static final Logger log = LoggerFactory.getLogger(SysPostServiceImpl.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search = (params.get("search") == null) ? "" : params.get("search").toString();

        //调用自封装的分页查询工具
        IPage<ProcCategoryEntity> queryPage = new QueryUtil<ProcCategoryEntity>().getQueryPage(params);

        QueryWrapper wrapper = new QueryWrapper<ProcCategoryEntity>()
                .like(StringUtils.isNotBlank(search), "category_name", search.trim())
                .or(StringUtils.isNotBlank(search))
                .like(StringUtils.isNotBlank(search), "category_code", search.trim())
                .orderByAsc("order_num");

        IPage<ProcCategoryEntity> resPage = this.page(queryPage, wrapper);
        //获取用户所属的部门、用户的岗位信息
        SysUserEntity user;
        for (ProcCategoryEntity category : resPage.getRecords()) {
            try {
                user = sysUserService.getById(category.getCreaterId());
                category.setCreaterName(user.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new PageUtil(resPage);
    }


    //新增
    @Override
    public void saveCategory(ProcCategoryEntity categoryEntity) {
        //判断编码是否重复
        if (this.getOne(new QueryWrapper<ProcCategoryEntity>().eq("category_code", categoryEntity.getCategoryCode())) != null) {
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        String uuid = UUID.randomUUID().toString();
        categoryEntity.setId(uuid);
        categoryEntity.setCreaterId((long) 1);
        categoryEntity.setCreateTime(DateTime.now().toDate());
        save(categoryEntity);

    }
}