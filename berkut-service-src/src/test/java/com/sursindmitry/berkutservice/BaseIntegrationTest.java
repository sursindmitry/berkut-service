package com.sursindmitry.berkutservice;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import com.sursindmitry.berkutservice.config.UtilConfig;
import com.sursindmitry.berkutservice.initializers.KeycloakInitializer;
import com.sursindmitry.berkutservice.initializers.PostgresInitializer;
import com.sursindmitry.berkutservice.initializers.RedisInitializer;
import com.sursindmitry.berkutservice.util.AuthUtil;
import com.sursindmitry.commonmodels.util.JsonParserUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
    KeycloakInitializer.class,
    PostgresInitializer.class,
    RedisInitializer.class
})
@AutoConfigureWebTestClient(timeout = "36000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@Import(UtilConfig.class)
@DBRider
@DBUnit(caseSensitiveTableNames = true, allowEmptyFields = true, leakHunter = true)
public abstract class BaseIntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected AuthUtil authUtil;

    @SpyBean
    protected JsonParserUtil jsonParserUtil;
}
