package com.vansven.api.domain.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {
    /**
     * 状态码
     */
    private int code;
    /**
     * 状态码描述
     */
    private String description;
    /**
     * 要返回给前端的通用类型数据
     */
    private T data;

    public BaseResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public BaseResponse(int code, String description, T data) {
        this.code = code;
        this.description = description;
        this.data = data;
    }

}
