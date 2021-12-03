package io.takima.reservation.utils.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MonitoringAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Around("@annotation(monitored)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, Monitored monitored) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        if (monitored.uuidPosition() < 0) {
            LOGGER.info("Method {} executed in {} ms.",
                    joinPoint.getSignature(),
                    System.currentTimeMillis() - startTime);
        } else {
            // We know the position of the correlator in the argument list
            LOGGER.info("[{}] Method {} executed in {} ms.",
                    joinPoint.getArgs()[monitored.uuidPosition()],
                    joinPoint.getSignature(),
                    System.currentTimeMillis() - startTime);
        }
        return proceed;
    }

}
