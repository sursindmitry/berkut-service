package com.sursindmitry.berkutservice.initializers;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {


    @Container
    public static final RedisContainer container = new RedisContainer(
        DockerImageName.parse("redis:7.0-alpine")
    );

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();

        TestPropertyValues.of(
            "spring.data.redis.host=" + container.getHost(),
            "spring.data.redis.port=" + container.getFirstMappedPort()
        ).applyTo(applicationContext.getEnvironment());

    }
}
