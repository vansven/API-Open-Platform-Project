package com.vansven.api.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
@ApiModel(value = "用户信息实体类")
public class UserInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "用户账号")
    @TableField("user_account")
    private String userAccount;

    @ApiModelProperty(value = "用户密码")
    @TableField("user_password")
    private String userPassword;

    @ApiModelProperty(value = "用户头像")
    @TableField("user_avatar")
    private String userAvatar;

    @ApiModelProperty(value = "是否为管理员，0-不是，1-是")
    @TableField("is_admin")
    private Integer isAdmin;

    @ApiModelProperty(value = "公钥")
    @TableField("public_key")
    private String publicKey;

    @ApiModelProperty(value = "私钥")
    @TableField("private_key")
    private String privateKey;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除，0-没删除 1-删除")
    @TableField("is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}