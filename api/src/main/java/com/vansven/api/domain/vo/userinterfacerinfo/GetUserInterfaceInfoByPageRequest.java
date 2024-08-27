package com.vansven.api.domain.vo.userinterfacerinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetUserInterfaceInfoByPageRequest {

    /**
     * 查询的页数
     */
    @ApiModelProperty(value = "查询的页数", example = "1",required = true)
    private Integer pageNumber;

    /**
     * 每页查询的数量
     */
    @ApiModelProperty(value = "每页查询的数量",example = "3",required = true)
    private Integer pageSize;

}
