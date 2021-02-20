package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.ProcCategoryEntity;
import com.debug.pmp.model.entity.ProcMoldEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.ProcMoldDao;
import com.debug.pmp.server.service.ProcCategoryService;
import com.debug.pmp.server.service.ProcMoldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pawell
 * @since 2021-02-18
 */
@Service("ProcMoldService")
public class ProcMoldServiceImpl extends ServiceImpl<ProcMoldDao, ProcMoldEntity> implements ProcMoldService {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProcCategoryService procCategoryService;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search = (params.get("search") == null) ? "" : params.get("search").toString();

        //调用自封装的分页查询工具
        IPage<ProcMoldEntity> queryPage = new QueryUtil<ProcMoldEntity>().getQueryPage(params);

        QueryWrapper wrapper = new QueryWrapper<ProcMoldEntity>()
                .like(StringUtils.isNotBlank(search), "mold_name", search.trim())
                .or(StringUtils.isNotBlank(search))
                .like(StringUtils.isNotBlank(search), "mold_code", search.trim())
                .orderByAsc("order_num");

        IPage<ProcMoldEntity> resPage = this.page(queryPage, wrapper);
        //获取用户所属的部门、用户的岗位信息
        SysUserEntity user;
        ProcCategoryEntity category;
        for (ProcMoldEntity moldEntity : resPage.getRecords()) {
            try {
                user = sysUserService.getById(moldEntity.getCreaterId());
                category = procCategoryService.getById(moldEntity.getCategoryId());
                moldEntity.setCreaterName(user.getName());
                moldEntity.setCategoryName(category.getCategoryName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new PageUtil(resPage);
    }

    //新增
    @Override
    public void saveProMold(ProcMoldEntity procMoldEntity) {
        //判断编码是否重复
        if (this.getOne(new QueryWrapper<ProcMoldEntity>().eq("mold_code", procMoldEntity.getMoldCode())) != null) {
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        String uuid = UUID.randomUUID().toString();
        procMoldEntity.setId(uuid);
        Long userId = ShiroUtil.getUserId();
        procMoldEntity.setCreaterId(userId);
        procMoldEntity.setCreateTime(DateTime.now().toDate());
        save(procMoldEntity);


    }

    @Override
    public ProcMoldEntity getMoldInfo(String id) {
        ProcMoldEntity procMoldEntity = this.getById(id);
        return procMoldEntity;
    }

    @Override
    public void updateMold(ProcMoldEntity procMoldEntity) {
        ProcMoldEntity oldMoldEntity = this.getById(procMoldEntity.getId());
        if (oldMoldEntity != null && !oldMoldEntity.getMoldCode().equals(procMoldEntity.getMoldCode())){
           String  moldCode =   procMoldEntity.getMoldCode();
            QueryWrapper<ProcMoldEntity> wrapper = new QueryWrapper<ProcMoldEntity>().eq("mold_code",moldCode);
            ProcMoldEntity one = this.getOne(wrapper);
            if(one != null){
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }
        procMoldEntity.setReviserTime(DateTime.now().toDate());
        this.updateById(procMoldEntity);
    }

    @Override
    public void deleteMold(List<String> ids) {
        String idsStr = StringUtils.join(ids, ",");
        String sqlStr = CommonUtil.concatStrToChar(idsStr, ",");
        baseMapper.removeByMoldIds(sqlStr);
    }
}
