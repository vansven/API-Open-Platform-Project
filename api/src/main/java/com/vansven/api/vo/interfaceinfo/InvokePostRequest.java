package com.vansven.api.vo.interfaceinfo;

import lombok.Data;
import neu.vansven.apiclientsdk.domain.Person;

@Data
public class InvokePostRequest {
    /**
     * 接口id
     */
    Long id;
    /**
     * api2请求参数
     */
    Person person;
    /**
     * 有接口需要签名认证的时候提供公钥
     */
    String publicKey;
    /**
     * 有接口需要签名认证的时候提供私钥
     */
    String privateKey;


}
