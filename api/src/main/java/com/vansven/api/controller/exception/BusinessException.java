package com.vansven.api.controller.exception;

public class BusinessException extends RuntimeException {
    private int code;
    private String description;
    public BusinessException(int code, String description){
        this.code = code;
        this.description = description;
    }

    public BusinessException(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

