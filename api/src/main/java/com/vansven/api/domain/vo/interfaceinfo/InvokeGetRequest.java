package com.vansven.api.domain.vo.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InvokeGetRequest {
    /**
     * 接口id
     */
    @ApiModelProperty(value = "接口id",required = true,example = "7")
    Long id;
    /**
     * api1请求参数
     */
    @ApiModelProperty(value = "api1请求参数",required = true, example = "fansiwen")
    String name;
    /**
     * 公钥
     */
    @ApiModelProperty(value = "公钥", required = true, example = "vansven1")
    String publicKey;
    /**
     * 私钥
     */
    @ApiModelProperty(value = "私钥", required = true, example = "xxxxAi")
    String privateKey;

}
