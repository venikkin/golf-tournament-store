package com.venikkin.example.golftmts.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = [
            "providers.source=file:conf/providers.example.json"
        ]
)
class GolfTournamentsControllerIT {

    @LocalServerPort
    var serverPort: Int = 0;

    @Test
    fun `test ping`() {
        val request = HttpRequest.newBuilder()
                .GET()
                .uri(URI("http://localhost:$serverPort/golf/tournaments"))
                .build()

        val httpClient = HttpClient.newHttpClient()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        Assertions.assertEquals(200, response.statusCode())
    }
}