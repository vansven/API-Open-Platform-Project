package com.vansven.api.domain.vo.userinterfacerinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateUserInterfaceRequest {

    /**
     * 根据主键id更新某条记录
     */
    @ApiModelProperty(value = "主键",example = "1", required = true)
    private Long id;

    /**
     * 用户总调用接口次数
     */
    @ApiModelProperty("用户总调用接口次数")
    private Integer totalNum;

    /**
     * 用户剩余调用接口次数
     */
    @ApiModelProperty(value = "用户每次购买后剩余调用接口次数",required = true)
    private Integer leftNum;

    /**
     * 是否允许用户调用接口,0 - 不允许， 1 - 允许（默认）
     */
    @ApiModelProperty("是否允许用户调用接口")
    private Integer isPermite;

}
