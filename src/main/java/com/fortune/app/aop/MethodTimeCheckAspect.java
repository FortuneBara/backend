package com.fortune.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodTimeCheckAspect {

    @Around("@annotation(com.fortune.app.common.aop.MethodTimeCheck)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        System.out.println(methodName + " 실행 시간: " + (end - start) + "ms");
        return result;
    }
}
