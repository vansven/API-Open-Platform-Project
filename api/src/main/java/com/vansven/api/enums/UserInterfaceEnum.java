package com.vansven.api.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public enum UserInterfaceEnum {
    OPENAPI_DEFAULT_COUNT(10, "管理员给用户默认开通的调用次数");

    @ApiModelProperty("次数")
    private Integer count;

    @ApiModelProperty("描述")
    private String description;

    UserInterfaceEnum(Integer count, String description){
        this.count = count;
        this.description = description;
    }


}
