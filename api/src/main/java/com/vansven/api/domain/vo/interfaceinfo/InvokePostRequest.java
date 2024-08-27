package com.vansven.api.domain.vo.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import neu.vansven.apiclientsdk.domain.Person;

@Data
public class InvokePostRequest {
    /**
     * 接口id
     */
    @ApiModelProperty(value = "接口id", required = true, example = "8")
    Long id;
    /**
     * api2请求参数
     */
    @ApiModelProperty(value = "api2请求参数", required = true)
    Person person;
    /**
     * 有接口需要签名认证的时候提供公钥
     */
    @ApiModelProperty(value = "公钥", required = true, example = "fansiwen")
    String publicKey;
    /**
     * 有接口需要签名认证的时候提供私钥
     */
    @ApiModelProperty(value = "私钥", required = true, example = "vansven")
    String privateKey;


}
