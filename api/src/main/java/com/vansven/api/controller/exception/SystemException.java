package com.vansven.api.controller.exception;

public class SystemException extends RuntimeException {
    private int code;
    private String description;
    public SystemException(int code, String description){
        super(description);
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

