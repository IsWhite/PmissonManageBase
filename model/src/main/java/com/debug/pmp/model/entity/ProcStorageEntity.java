package com.debug.pmp.model.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * <p>
 * 
 * </p>
 *
 * @author pawell
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("proc_storage")
@ApiModel(value="ProcStorageEntity对象", description="")
public class ProcStorageEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id",type = IdType.INPUT) //mybatis type = IdType.INPUT,没有的话无法插入主键
    private String id;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank(message ="仓库名称不能为空!" )
    private String storageName;

    @ApiModelProperty(value = "仓库编号")
    @NotBlank(message ="仓库编号不能为空!" )
    private String storageCode;

    @ApiModelProperty(value = "申请单状态(-1草稿，1，流转，2办结，3作废)")
    private Integer itsmStatus;

    @ApiModelProperty(value = "流程ID")
    private String flowId;

    @ApiModelProperty(value = "审批人ID")
    private Long approverId;

    @ApiModelProperty(value = "仓库面积")
    @Min(value = 0 ,message = "仓库面积不能小于0") //数字不能小于0
    private BigDecimal storageArea;

    @ApiModelProperty(value = "租期")
    @Min(value = 0 ,message = "租期不能小于0") //数字不能小于0
    private String rentTime;

    @ApiModelProperty(value = "起租日期")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date staRentDate;

    @ApiModelProperty(value = "到租日期")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date endRentDate;

    @ApiModelProperty(value = "租金")
    @Min(value = 0 ,message = "租金不能小于0") //数字不能小于0
    private BigDecimal rentPrice;

    @ApiModelProperty(value = "租户ID")
    private Long renterId;

    @TableField(exist = false) //必须存在的非本表中的字段
    private String renterName; //创建人名称

    @ApiModelProperty(value = "租户电话")
    private String renterPhone;

    @ApiModelProperty(value = "房东ID")
    private Long landlordId;

    @TableField(exist = false) //必须存在的非本表中的字段
    private String landlordName; //创建人名称

    @ApiModelProperty(value = "房东电话")
    private String landlordPhone;

    @ApiModelProperty(value = "合同URL")
    private String contractUrl;

    @ApiModelProperty(value = "仓库地址")
    private String storageAddress;

    @ApiModelProperty(value = "仓库门牌号")
    private String storageDoornum;

    @ApiModelProperty(value = "是否到期 0 未到期")
    private Integer isExpire;

    @ApiModelProperty(value = "创建人ID")
    private Long createrId;

    @TableField(exist = false) //必须存在的非本表中的字段
    private String createrName; //创建人名称

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date reviserTime;



}
