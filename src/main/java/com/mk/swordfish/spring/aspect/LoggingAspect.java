package com.mk.swordfish.spring.aspect;

import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Pointcut("""
      @within(org.springframework.stereotype.Repository)
      || @within(org.springframework.stereotype.Service)
      || @within(org.springframework.web.bind.annotation.RestController)
      || @within(org.springframework.stereotype.Component)
      || @within(com.mk.swordfish.core.annotations.Behavior)
       """
  )
  public void springBeanPointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  @Pointcut("within(com.mk..*)")
  public void applicationPackagePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  @Around("applicationPackagePointcut() && springBeanPointcut() && execution(* *(..))")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Logger log = getLogger(joinPoint.getTarget(), "log");
    if (!log.isDebugEnabled()) {
      return joinPoint.proceed();
    }
    String className = getClassName(joinPoint);
    String methodName = joinPoint.getSignature().getName();

    boolean isCompletable = CompletionStage.class.isAssignableFrom(
        methodSignature.getMethod().getReturnType());

    try {
      log.debug("In [{}.{}]", className, methodName);
      Object result = joinPoint.proceed();

      if (isCompletable) {
        ((CompletionStage) result).whenComplete(
            (BiConsumer<Object, Throwable>) (resp, throwable) -> {
              if (throwable == null) {
                log.debug("Out [{}.{}]", className, methodName);
              } else {
                log.debug("Out [{}.{}] Exception:[{}] Message:[{}]", className, methodName,
                    throwable.getClass(),
                    throwable.getMessage());
              }
            });
      } else {
        log.debug("Out [{}]:[{}]", className, methodName);
      }
      return result;

    } catch (Throwable e) {
      log.debug("Out [{}]:[{}] Exception:[{}][{}]", className, methodName,
          e.getClass(),
          e.getMessage());
      throw e;
    }
  }

  protected Logger getLogger(Object target, String logName) {
    Logger log;
    try {
      log = (Logger) FieldUtils.readField(target, logName, true);
    } catch (IllegalArgumentException | IllegalAccessException | ClassCastException ex) {
      log = logger;
    }
    return log;
  }

  protected String getClassName(JoinPoint joinPoint) {
    String className;
    Object target = joinPoint.getTarget();

    if (target != null) {
      className = target.getClass().getSimpleName();
    } else {
      className = joinPoint.getStaticPart().getSignature().getDeclaringType().getSimpleName();
    }
    return className;
  }
}
