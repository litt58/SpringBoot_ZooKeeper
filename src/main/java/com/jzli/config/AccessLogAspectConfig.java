package com.jzli.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AccessLogAspectConfig {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogAspectConfig.class);

    private ThreadLocal<Long> startTimeRecorder = new ThreadLocal<>();

    @Pointcut("execution(public * com.jzli.controller..*.*(..))")
    public void access() {
    }

    @Before("access()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        //记录开始时间
        startTimeRecorder.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("REQUEST ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "result", pointcut = "access()")
    public void doAfterReturning(Object result) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE RESULT: " + result);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTimeRecorder.get()) + "ms");

    }

}