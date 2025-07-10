package com.sursindmitry.berkutservice.config;

import com.sursindmitry.berkutservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Компонент для синхронизации ролей из Keycloak при запуске приложения.
 * Запускает синхронизацию после полной инициализации приложения.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakRoleSyncInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleService roleService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        log.info("Приложение запущено, начинаем синхронизацию ролей Keycloak");

        roleService.syncRoles();
    }
}
