package com.vansven.api.vo.interfaceinfo;

import lombok.Data;

@Data
public class UpdateInterRequest {
    /**
     *
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */
    private String responseParams;

    /**
     * 接口状态 0-关闭 1-开启 2-开发中
     */
    private Integer status;

}
