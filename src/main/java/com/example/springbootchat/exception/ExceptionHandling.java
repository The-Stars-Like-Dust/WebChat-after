package com.example.springbootchat.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandling {
    @ResponseBody
    @ExceptionHandler(PermissionIsAbnormal.class)
    public String handlePermissionIsAbnormal() {
        return "权限异常，你无权访问，请在正规途径获取权限后访问。";
    }

    @ResponseBody
    @ExceptionHandler({NullPointerException.class})
    public String handleNullPointerException() {
        return "请检查参数是否标准";
    }

}
