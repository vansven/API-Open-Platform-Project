package com.vansven.api.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口关系表
 * @TableName user_interface_info
 */
@TableName(value ="user_interface_info")
@Data
@ApiModel(value = "用户接口关系实体类")
public class UserInterfaceInfo implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    @TableField("id")
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 接口id
     */
    @TableField("interface_id")
    @ApiModelProperty("接口id")
    private Long interfaceId;


    /**
     * 记录用户总调用次数
     */
    @TableField("total_num")
    @ApiModelProperty("用户从开始开通到多次购买调用接口的总次数")
    private Integer totalNum;

    /**
     * 用户剩余调用接口次数
     */
    @TableField("left_num")
    @ApiModelProperty("用户每次购买后剩余调用接口次数")
    private Integer leftNum;

    /**
     * 是否允许用户调用接口,0 - 不允许， 1 - 允许
     */
    @TableField("is_permite")
    @ApiModelProperty("是否允许用户调用接口,0 - 不允许， 1 - 允许")
    private Integer isPermite;

    /**
     * 逻辑删除,0 - 不删除，1 - 删除
     */
    @TableLogic(value = "0",delval = "1")
    @TableField("is_delete")
    @ApiModelProperty("逻辑删除,0 - 不删除，1 - 删除")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}