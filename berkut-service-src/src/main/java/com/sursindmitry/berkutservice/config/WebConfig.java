package com.sursindmitry.berkutservice.config;

import com.sursindmitry.berkutservice.config.interceptor.RejectAuthenticatedUsersInterceptor;
import com.sursindmitry.berkutservice.constant.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация MVC для регистрации интерцепторов.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RejectAuthenticatedUsersInterceptor rejectAuthenticatedUsersInterceptor;

    /**
     * Регистрирует интерцептор {@link RejectAuthenticatedUsersInterceptor}
     * и применяет его к пути {@code /api/v1/public/register}.
     *
     * @param registry реестр интерцепторов Spring MVC
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(rejectAuthenticatedUsersInterceptor)
            .addPathPatterns(
                ApiConstant.PUBLIC_API + "/register"
            );
    }
}
