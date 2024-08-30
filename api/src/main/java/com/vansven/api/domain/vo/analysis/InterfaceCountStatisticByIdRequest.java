package com.vansven.api.domain.vo.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InterfaceCountStatisticByIdRequest {
    @ApiModelProperty(value = "接口id", required = true, example = "7")
    long interfaceId;
}
