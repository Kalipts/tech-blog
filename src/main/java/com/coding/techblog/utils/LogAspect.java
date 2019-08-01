//package com.coding.techblog.utils;
//
//
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class LogAspect {
////    private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class);
////
////    @Pointcut("execution(public * com.coding.techblog.controller..*.*(..))")
////    public void webLog(){}
////
////    @Before("webLog()")
////    public void doBefore(JoinPoint joinPoint) throws Throwable {
////        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
////        HttpServletRequest request = attributes.getRequest();
////
////        LOGGER.info("URL : " + request.getRequestURL().toString() + ",IP : " + request.getRemoteAddr() + ",CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + ",ARGS : " + Arrays.toString(joinPoint.getArgs()));
////    }
////
////    @AfterReturning(returning = "object", pointcut = "webLog()")
////    public void doAfterReturning(Object object) throws Throwable {
////        LOGGER.info("RESPONSE : " + object);
////    }
//}
