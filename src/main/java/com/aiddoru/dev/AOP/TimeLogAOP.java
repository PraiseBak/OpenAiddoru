package com.aiddoru.dev.AOP;

import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.praiseutil.timelog.utility.LogTrace;
import com.praiseutil.timelog.utility.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Log
public class TimeLogAOP {

    private final LogTrace logTrace;

    @Pointcut("execution(* com.aiddoru.dev.Service..*(..))")
    public void allService() {}

    @Pointcut("execution(* com.aiddoru.dev.Controller..*(..))")
    public void allController() {}

    @Around("allController() || allService()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus traceStatus = logTrace.begin(joinPoint.getSignature().toShortString());
        try {
            Object result = joinPoint.proceed();
            logTrace.end(traceStatus);
            return result;
        } catch (CustomException e) {
            logTrace.begin(e.getCommunityErrorCode().getMessage());
            logTrace.exception(traceStatus, e);
            throw e;
        }
    }
}