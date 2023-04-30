package com.github.gasfgrv.storage.configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestConfiguration {

    @Container
    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("files")
            .withPassword("postgres")
            .withUsername("postgres")
            .withReuse(true);

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.passoword", container::getPassword);
    }

}
