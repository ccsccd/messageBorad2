package org.wecan.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {

    private static Logger logger = LoggerFactory.getLogger(TestAspect.class);

    @Pointcut("execution(public * org.wecan.demo.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        logger.info("who: {}", joinPoint.getSignature().getDeclaringTypeName() + "  " + joinPoint.getSignature().getName());
        logger.info("aop-----before");
    }

    @After("log()")
    public void after() {
        logger.info("aop-----after");
    }

    @AfterReturning(value = "log()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {
        logger.info("aop-----afterReturning");
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object res = null;
        logger.info("aop-----around 1");
        res = pjp.proceed();
        logger.info("aop-----around 2");
        return res;
    }

    @AfterThrowing(value = "log()", throwing = "e")
    public void afterThrowing(Throwable e) {
        e.printStackTrace();
    }

}
