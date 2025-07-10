package com.sursindmitry.berkutservice.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурация {@link WebClient}.
 */
@Configuration
@Slf4j
public class WebClientConfig {

    /**
     * Базовая конфигурация бина {@link WebClient}.
     *
     * @param builder билдер веб клиента
     * @return {@link WebClient}
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        log.info("Конфигурация WebClientConfig");

        return builder
            .baseUrl("http://localhost:8080")
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .responseTimeout(Duration.ofSeconds(10))
            ))
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(10 * 1024 * 1024))
                .build())
            .build();
    }
}
