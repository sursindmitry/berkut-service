package com.sursindmitry.berkutservice.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 * Настраивает аутентификацию и авторизацию с использованием JWT токенов.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    private static final Set<String> PUBLIC_PATHS =
        Set.of("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**",
            "/swagger-resources/**", "/auth/**", "/oauth2/**", "/actuator/**", "/api/v1/public/**");

    /**
     * Создает конвертер для преобразования JWT токена в объект аутентификации.
     * Извлекает роли как из стандартных claims, так и из realm_access.
     *
     * @return сконфигурированный конвертер JWT токенов
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
            createGrantedAuthoritiesConverter();

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities =
                getGrantedAuthorities(grantedAuthoritiesConverter, jwt);

            addRealmRolesToAuthorities(jwt, authorities);

            return authorities;
        });

        return jwtAuthenticationConverter;
    }

    /**
     * Создает конвертер для преобразования JWT claims в права доступа.
     * Настраивает имя claim'а для ролей и префикс прав доступа.
     *
     * @return сконфигурированный конвертер прав доступа
     */
    private JwtGrantedAuthoritiesConverter createGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName(ROLES_CLAIM);
        converter.setAuthorityPrefix("");
        return converter;
    }

    /**
     * Получает права доступа из JWT токена, используя предоставленный конвертер.
     *
     * @param converter конвертер для преобразования JWT в права доступа
     * @param jwt       JWT токен
     * @return коллекция прав доступа
     */
    private Collection<GrantedAuthority> getGrantedAuthorities(
        JwtGrantedAuthoritiesConverter converter, Jwt jwt) {
        return converter.convert(jwt);
    }

    /**
     * Добавляет роли из realm_access в существующую коллекцию прав доступа.
     *
     * @param jwt         JWT токен
     * @param authorities коллекция прав доступа для дополнения
     */
    private void addRealmRolesToAuthorities(Jwt jwt, Collection<GrantedAuthority> authorities) {
        try {
            List<String> realmRoles = extractRealmRoles(jwt);
            log.debug("Extracted realm roles: {}", realmRoles);

            realmRoles.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);

            log.debug("Final authorities after adding realm roles: {}", authorities);
        } catch (Exception e) {
            log.error("Error adding realm roles to authorities", e);
        }
    }

    /**
     * Настраивает цепочку фильтров безопасности.
     * Конфигурирует CSRF, CORS, управление сессиями и правила доступа к endpoints.
     *
     * @param http конфигурация безопасности HTTP
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникла ошибка при конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers(PUBLIC_PATHS.toArray(String[]::new)).permitAll()
                    .anyRequest()
                    .authenticated()).oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))).build();
    }

    /**
     * Извлекает роли из секции realm_access JWT токена.
     * Обрабатывает возможные ошибки при извлечении и преобразовании данных.
     *
     * @param jwt JWT токен
     * @return список ролей или пустой список в случае ошибки
     */
    private List<String> extractRealmRoles(Jwt jwt) {
        try {
            Object realmAccess = jwt.getClaim(REALM_ACCESS_CLAIM);
            if (!(realmAccess instanceof Map)) {
                log.warn("Параметр realm_access не является Map: {}", realmAccess);
                return Collections.emptyList();
            }

            Map<String, Object> realmAccessMap = (Map<String, Object>) realmAccess;
            Object roles = realmAccessMap.get(ROLES_CLAIM);

            if (!(roles instanceof List)) {
                log.warn("Параметр roles не является List: {}", roles);
                return Collections.emptyList();
            }

            return (List<String>) roles;
        } catch (Exception e) {
            log.error("Ошибка при извлечении ролей из JWT токена", e);
            return Collections.emptyList();
        }
    }
}
