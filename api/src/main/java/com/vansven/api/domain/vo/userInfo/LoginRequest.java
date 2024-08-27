package com.vansven.api.domain.vo.userInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号",example = "vansven",required = true)
    private String userAccount;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码",example = "xxxxi_vans",required = true)
    private String userPassword;

}
