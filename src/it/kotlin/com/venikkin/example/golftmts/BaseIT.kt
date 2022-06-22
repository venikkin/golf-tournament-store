package com.venikkin.example.golftmts

import com.venikkin.example.golftmts.util.SchemaHelper
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.http.converters.preferred-json-mapper=gson",
        "providers.source=file:conf/providers.example.json",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect",
        "spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl",
        "spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
    ]
)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
abstract class BaseIT {

    companion object {

        const val SOURCE_1_TOKEN = "999d71a7-b91d-49f6-8f16-c036fccbb69d"
        const val SOURCE_2_TOKEN = "5a19733c-fdfb-4a3a-a792-bf406f135e88"

        @JvmStatic
        @Container
        val mysql = MySQLContainer("mysql:8.0.29")
            .withExposedPorts(3306)
            .withDatabaseName("golf")
            .withUsername("dev")
            .withPassword("dev-pwd")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mysql.jdbcUrl }
            registry.add("spring.datasource.password") { mysql.password }
            registry.add("spring.datasource.username") { mysql.username }
        }

        @JvmStatic
        @BeforeAll
        fun setUpJdbcUrl(@Autowired jdbcTemplate: JdbcTemplate) {
            SchemaHelper.applySchema(jdbcTemplate)
        }

    }

}