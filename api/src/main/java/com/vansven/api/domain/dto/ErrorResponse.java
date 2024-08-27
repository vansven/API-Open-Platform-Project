package com.vansven.api.domain.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    /**
     * 状态码
     */
    private int code;
    /**
     * 状态码描述
     */
    private String description;

    public ErrorResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
