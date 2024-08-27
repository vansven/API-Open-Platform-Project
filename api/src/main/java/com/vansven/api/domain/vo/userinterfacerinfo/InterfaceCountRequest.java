package com.vansven.api.domain.vo.userinterfacerinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InterfaceCountRequest {

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


}
