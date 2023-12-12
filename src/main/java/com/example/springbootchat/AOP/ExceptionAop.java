package com.example.springbootchat.AOP;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

@Component
@Aspect
public class ExceptionAop {
    private static BufferedWriter writer = null;
    private static FileWriter fileWriter = null;
    private static File file = new File("methodAction.log");

    static {
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer = new BufferedWriter(fileWriter);
    }

    @Around("execution(* com.example.springbootchat.controller.*  .*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws IOException {
        Signature signature = joinPoint.getSignature();
        if (signature.toString().contains("selectMessageByTime")) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                System.out.println("执行异常");
            }
        }
        Object[] args = joinPoint.getArgs();
        String s = "----------------------------------------环绕前置通知--------------------------------------------\n";
        String s1 = "方法:  " + signature + "---" + "执行时间:  " + new Timestamp(System.currentTimeMillis()) + "\n";
        String s2 = "参数:\n";
        StringBuilder s3 = new StringBuilder();
        for (Object arg : args) {
            s3.append(arg).append("\n");
        }
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println("执行异常");
        }
        String s4 = "---------------------------------------环绕后置通知--------------------------------------------\n\n\n";
        if (file.length() > 1024 * 1024 * 10) {
            writer.close();
            writer = null;
            fileWriter.close();
            fileWriter = null;
            file.delete();
            fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
        }
        writer.write(s + s1 + s2 + s3 + s4);
        return proceed;
    }

}
