package com.springbatch.banksystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	@Pointcut("within(com.springbatch.banksystem..*) && !within(com.springbatch.banksystem.jwt..*)")
	public void logMethodPointcut() {}


	@Before("logMethodPointcut()")
	public void getlogMethodBeforeExecution(JoinPoint joinPoint) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// Get intercepted method details
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();

		// Log method
		log.info("Banking System Logging AOP : Before " + className + "." + methodName);
	}

	@After("logMethodPointcut()")
	public void getlogMethodAfterExecution(JoinPoint joinPoint) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// Get intercepted method details
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();

		// Log method
		log.info("Banking System Logging AOP : After " + className + "." + methodName);
	}
}
