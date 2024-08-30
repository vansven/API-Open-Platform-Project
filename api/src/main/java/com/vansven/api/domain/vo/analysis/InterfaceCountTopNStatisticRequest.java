package com.vansven.api.domain.vo.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InterfaceCountTopNStatisticRequest {
    @ApiModelProperty(value = "获取调用次数为top n的接口信息",required = true,example = "3")
    Integer topN;
}
