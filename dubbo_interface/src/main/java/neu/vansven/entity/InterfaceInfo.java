package neu.vansven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName(value ="interface_info")
@Data
@ApiModel(value = "接口信息实体类")
public class InterfaceInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "创建人id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "接口名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "接口描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "接口地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "请求方法")
    @TableField("method")
    private String method;

    @ApiModelProperty(value = "请求参数")
    @TableField("request_params")
    private String requestParams;

    @ApiModelProperty(value = "响应参数")
    @TableField("response_params")
    private String responseParams;

    @ApiModelProperty(value = "接口状态 0-关闭 1-开启 2-开发中")
    @TableField("status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 0- 没有 1-删除")
    @TableField("is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}