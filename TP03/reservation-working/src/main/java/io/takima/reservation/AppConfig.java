package io.takima.reservation;

import io.takima.reservation.utils.ErrorGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class AppConfig implements WebMvcConfigurer {

    private ErrorGenerator interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
