package com.jzli.config;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class OrderAspectConfig {

    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.jzli.controller..*.*(..))")
    public void access() {
    }

    @Before("access()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        logger.info("############################################################");
    }

    @AfterReturning(returning = "result", pointcut = "access()")
    public void doAfterReturning(Object result) throws Throwable {
        // 处理完请求，返回内容
        logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

}