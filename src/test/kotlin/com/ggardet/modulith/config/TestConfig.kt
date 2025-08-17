package com.ggardet.modulith.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class TestConfig {
    companion object {
        private val POSTGRES_IMAGE = DockerImageName.parse("postgres:15-alpine")
    }

    @Bean
    @ServiceConnection
    fun postgreSQLContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(POSTGRES_IMAGE)
            .withDatabaseName("modulith_test")
            .withUsername("test_user")
            .withPassword("test_password")
            .withReuse(true)
    }
}
