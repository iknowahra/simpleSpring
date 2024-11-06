package com.spring.global.aop;

import com.spring.global.helper.LoggingBuilder;
import com.spring.global.helper.LoggingBuilder.LogColors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Controller와 Service 메서드의 실행을 모니터링하고 로그를 기록하는 Aspect
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAOP {
    private final LoggingBuilder loggingBuilder;

    // GLOBAL
    @Pointcut("execution(* com.spring.loginMvc.controller..*(..))")
    public void globalControllerMethods() {
    }

    @Pointcut("execution(* com.spring.loginMvc.service..*(..))")
    public void globalServiceMethods() {
    }

    @Around("(globalControllerMethods())")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {

        String signature = joinPoint.getSignature().toString();
        String extractSignature = signature.substring(signature.lastIndexOf('.', signature.lastIndexOf('.') - 1) + 1);
        try {

            // Before method execution
            loggingBuilder.appendTitle(LogColors.MAGENTA, "-- Controller START: {} ", extractSignature);
            loggingBuilder.append(LogColors.MAGENTA, "# Controller Method: {} ", joinPoint.getSignature());

            // Log Argument
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg != null) {
                    loggingBuilder.append(LogColors.MAGENTA, "# Argument: " + arg.toString());
                }
            }

            // Proceed with the method execution
            Object result = joinPoint.proceed();

            // After method execution
            loggingBuilder.appendTitle(LogColors.MAGENTA, "-- Controller END: {} ", extractSignature);
            return result;

        } catch (Throwable throwable) {
            // Log the exception
            loggingBuilder.appendTitle(LogColors.RED, "-- Controller ERROR: {} ", extractSignature);
            loggingBuilder.append(LogColors.RED, "# Exception: {} ", throwable.getMessage());
            throw throwable; // Re-throw the exception after logging
        }
    }

    @Around("globalServiceMethods()")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {

        String signature = joinPoint.getSignature().toString();
        String extractSignature = signature.substring(signature.lastIndexOf('.', signature.lastIndexOf('.') - 1) + 1);

        // Before method execution
        loggingBuilder.appendTitle(LogColors.BLUE, "-- Service START: {} ", extractSignature);
        loggingBuilder.append(LogColors.BLUE, "# Service Method: {} ", joinPoint.getSignature());

        // Log Argument
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                loggingBuilder.append(LogColors.BLUE, "# Argument: " + arg.toString());
            }
        }

        // Proceed with the method execution
        Object result = joinPoint.proceed();

        // After method execution
        loggingBuilder.appendTitle(LogColors.BLUE, "-- Service END: {} ", extractSignature);

        return result;
    }
}
