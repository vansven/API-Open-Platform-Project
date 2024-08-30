package com.vansven.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InterfaceCountStatistic {

    @ApiModelProperty(value = "接口id")
    long interfaceId;

    @ApiModelProperty(value = "接口调用次数总和")
    long interfaceCountSum;
}
