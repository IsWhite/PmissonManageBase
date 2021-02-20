package com.debug.pmp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2021-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("proc_mold")
@ApiModel(value="ProcMoldEntity对象", description="")
public class ProcMoldEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id",type = IdType.INPUT) //mybatis type = IdType.INPUT,没有的话无法插入主键
    private String id;

    @ApiModelProperty(value = "类别ID")
    @NotBlank(message ="商品类别不能为空!" )
    private String categoryId;

    @TableField(exist = false) //必须存在的非本表中的字段,类别名称
    private String categoryName;

    @ApiModelProperty(value = "类型名称")
    @NotBlank(message ="类别名称不能为空!" )
    private String moldName;

    @ApiModelProperty(value = "类型编码")
    @NotBlank(message ="类别编号不能为空!" )
    private String moldCode;

    @ApiModelProperty(value = "类型图片")
    private String moldImg;

    @ApiModelProperty(value = "创建人ID")
    private Long createrId;

    @TableField(exist = false) //必须存在的非本表中的字段
    private String createrName; //创建人名称

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date reviserTime;

    @ApiModelProperty(value = "排序号")
    @Min(value = 0 ,message = "排序号不能小于0") //数字不能小于0
    private Integer orderNum;

    @ApiModelProperty(value = "是否启用 1 启用")
    private Integer isqy;


}
