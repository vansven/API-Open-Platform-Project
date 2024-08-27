package com.vansven.api.controller.handel;

import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.domain.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandle {

    @ExceptionHandler(BusinessException.class)
    public ErrorResponse doBusinessHandler(BusinessException e){
        return new ErrorResponse(e.getCode(),e.getDescription());
    }

}
