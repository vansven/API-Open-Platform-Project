package com.vansven.api.domain.vo.userinterfacerinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddUserInterfaceRequest {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id",example = "1",required = true)
    private Long userId;

    /**
     * 接口id
     */
    @ApiModelProperty(value = "接口id",example = "1",required = true)
    private Long interfaceId;


    /**
     * 用户剩余调用接口次数
     */
    @ApiModelProperty(value = "用户每次购买后剩余调用接口次数",example = "10",required = true)
    private Integer leftNum;


}
