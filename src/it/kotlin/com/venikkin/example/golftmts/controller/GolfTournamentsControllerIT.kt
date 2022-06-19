package com.venikkin.example.golftmts.controller

import com.venikkin.example.golftmts.util.SchemaHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

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
class GolfTournamentsControllerIT {

    companion object {

        const val SOURCE_1_TOKEN = "999d71a7-b91d-49f6-8f16-c036fccbb69d"

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

    @LocalServerPort
    var serverPort: Int = 0;

    @Test
    fun `client can insert tournament information on behalf of Source1 provider`() {
        // when
        val response = insertPayload("""
              {
                  "tournamentId": "174638",
                  "tournamentName": "Women's Open Championship",
                  "forecast": "fair",
                  "courseName": "Sunnydale Golf Course",
                  "countryCode": "GB",
                  "startDate": "09/07/21",
                  "endDate": "13/07/21",
                  "roundCount": "4"
              }
        """.trimIndent(), SOURCE_1_TOKEN)

        // then
        assertEquals(HttpStatus.OK.value(), response.statusCode())
    }


    private fun insertPayload(payload: String, providerToken: String?): HttpResponse<String> {
        val requestBuilder = HttpRequest.newBuilder()
            .POST(BodyPublishers.ofString(payload))
            .uri(URI("http://localhost:$serverPort/golf/tournaments"))
            .header("Content-Type", "application/json")

        if (providerToken != null) {
            requestBuilder.header("Provider-Token", providerToken)
        }

        return HttpClient.newHttpClient().send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
    }
}