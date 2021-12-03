package io.takima.reservation.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Error generator for api
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Component
public class ErrorGenerator implements HandlerInterceptor {

    /**
     * Errors frequence
     */
    static final int ERROR_FREQ = 3;
    final AtomicInteger requestCounter = new AtomicInteger();
    @Value("${config.error.generator.enable}")
    private String enablingErrorGenerator;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (enablingErrorGenerator.equals("true") && requestCounter.getAndIncrement() == ERROR_FREQ) {
            requestCounter.set(0);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Oops");
            return false;
        }
        return true;
    }
}