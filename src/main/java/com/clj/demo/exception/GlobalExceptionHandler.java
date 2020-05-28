package com.clj.demo.exception;

import com.clj.demo.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = OperateException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO operateErrorHandler(HttpServletRequest req, OperateException e) {

        return new ErrorDTO(400,e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO AuthorizationHandler(HttpServletRequest req, AuthorizationException e) {

        return new ErrorDTO(403,e.getMessage());
    }

    @ExceptionHandler(value = BaseUserException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorDTO BaseuserErrorHandler(HttpServletRequest req, BaseUserException e) {

        return new ErrorDTO(200,e.getMessage());
    }

    @ExceptionHandler(value = BaseDataException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorDTO PostErrorHandler(HttpServletRequest req, BaseDataException e) {

        return new ErrorDTO(200,e.getMessage());
    }
}
