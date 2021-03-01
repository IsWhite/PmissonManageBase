package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.ProcStorageEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.ProcStorageDao;
import com.debug.pmp.server.service.ProcStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Autowired
    private SysUserService sysUserService;


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
        SysUserEntity createrUser;
        SysUserEntity renterUser;
        SysUserEntity landlordUser;
        for (ProcStorageEntity storageEntity : resPage.getRecords()) {
            try {
                createrUser = sysUserService.getById(storageEntity.getCreaterId());
                renterUser = sysUserService.getById(storageEntity.getRenterId());
                landlordUser= sysUserService.getById(storageEntity.getLandlordId());
                if (createrUser != null) {
                    storageEntity.setCreaterName(createrUser.getName());
                }

                if (renterUser != null) {
                    storageEntity.setRenterName(renterUser.getName());
                }
                if (landlordUser != null) {
                    storageEntity.setLandlordName(landlordUser.getName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new PageUtil(resPage);
    }

    @Override
    public void saveStorage(ProcStorageEntity procStorageEntity) {
        //判断编码是否重复
        if (this.getOne(new QueryWrapper<ProcStorageEntity>().eq("storage_code", procStorageEntity.getStorageCode())) != null) {
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        String uuid = UUID.randomUUID().toString();
        procStorageEntity.setId(uuid);
        Long userId = ShiroUtil.getUserId();
        procStorageEntity.setCreaterId(userId);
        procStorageEntity.setCreateTime(DateTime.now().toDate());
        save(procStorageEntity);


    }

    @Override
    public ProcStorageEntity getInfoById(String id) {

        ProcStorageEntity procStorageEntity = this.getById(id);
//
//        Long renterUserId = sysUserService.getById(procStorageEntity.getRenterId()).getUserId();
//        Long landlordUserId = sysUserService.getById(procStorageEntity.getLandlordId()).getUserId();


        return procStorageEntity;
    }

    @Override
    public void updateStorage(ProcStorageEntity storageEntity) {
        String oldCode = this.getById(storageEntity.getId()).getStorageCode();
        if (oldCode != null && !oldCode.equals(storageEntity.getStorageCode())) {
           String  code =   storageEntity.getStorageCode();
            QueryWrapper<ProcStorageEntity> wrapper = new QueryWrapper<ProcStorageEntity>().eq("storage_code",code);
            ProcStorageEntity en = this.getOne(wrapper);
            if (en != null){
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }
        storageEntity.setReviserTime(DateTime.now().toDate());
        this.updateById(storageEntity);
    }

    //删除
    @Override
    public void deleteByIds(List<String> ids) {
        String idsStr = StringUtils.join(ids,",");
        String sqlIds = CommonUtil.concatStrToChar(idsStr, ",");
        baseMapper.deleteByIds(sqlIds);


    }
}
