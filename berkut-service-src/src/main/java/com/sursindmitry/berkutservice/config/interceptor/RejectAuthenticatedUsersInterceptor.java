package com.sursindmitry.berkutservice.config.interceptor;

import com.sursindmitry.commonmodels.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Интерцептор для проверки входа в систему.
 */
@Component
public class RejectAuthenticatedUsersInterceptor implements HandlerInterceptor {

    /**
     * Проверяет, вошёл ли пользователь в систему.
     *
     * @param request  запрос
     * @param response ответ
     * @param handler  обработчик запроса
     * @return false если пользователь вошёл
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
            && !(auth instanceof AnonymousAuthenticationToken)) {

            throw new AccessDeniedException(
                "Вы уже вошли в систему и не можете это использовать"
            );
        }

        return true;
    }
}
