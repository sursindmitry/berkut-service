package com.sursindmitry.berkutservice.initializers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    public static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("test")
        .withPassword("test")
        .withPassword("test")
        .withUrlParam("currentSchema", "berkut_service")
        .withReuse(false);

    public static final String DATASOURCE_URL = "spring.datasource.url=";
    public static final String DATASOURCE_USERNAME = "spring.datasource.username=";
    public static final String DATASOURCE_PASSWORD = "spring.datasource.password=";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        CONTAINER.start();

        try (Connection connection = DriverManager.getConnection(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS berkut_service");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create schema", e);
        }

        TestPropertyValues.of(
            DATASOURCE_URL + CONTAINER.getJdbcUrl(),
            DATASOURCE_USERNAME + CONTAINER.getUsername(),
            DATASOURCE_PASSWORD + CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
