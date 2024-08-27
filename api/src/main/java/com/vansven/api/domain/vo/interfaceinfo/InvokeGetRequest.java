package com.vansven.api.domain.vo.interfaceinfo;

import lombok.Data;

@Data
public class InvokeGetRequest {
    /**
     * 接口id
     */
    Long id;
    /**
     * api1请求参数
     */
    String name;

}
