package com.venikkin.example.golftmts.controller

import com.venikkin.example.golftmts.BaseIT
import com.venikkin.example.golftmts.provider.source1.Source1PayloadConverter
import com.venikkin.example.golftmts.provider.source2.Source2PayloadConverter
import com.venikkin.example.golftmts.repository.TournamentRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.time.LocalDate

class GolfTournamentsControllerIT : BaseIT() {

    @LocalServerPort
    var serverPort: Int = 0;

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @AfterEach
    fun cleanUp() {
        tournamentRepository.deleteAll()
    }

    @Test
    fun `client can insert tournament information on behalf of Source1 provider`() {
        // given
        val tournamentId = "174638"

        // when
        val response = insertPayload("""
              {
                  "tournamentId": "$tournamentId",
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

        val actual = tournamentRepository.findByProviderAndExternalId(Source1PayloadConverter.PROVIDER_NAME, tournamentId)
        assertTrue(actual.isPresent)
        assertEquals(tournamentId, actual.get().externalId)
        assertEquals(Source1PayloadConverter.PROVIDER_NAME, actual.get().provider)
        assertEquals("Women's Open Championship", actual.get().name)
        assertEquals("fair", actual.get().forecast)
        assertEquals("Sunnydale Golf Course", actual.get().courseName)
        assertEquals("GB", actual.get().countryCode)
        assertEquals(LocalDate.of(2021, 7, 9), actual.get().startDate)
        assertEquals(LocalDate.of(2021, 7, 13), actual.get().endDate)
        assertEquals(4, actual.get().rounds)
    }

    @Test
    fun `client can insert tournament information on behalf of Source2 provider`() {
        // given
        val tournamentId = "87fc6650-e114-4179-9aef-6a9a13030f80"

        // when
        val response = insertPayload("""
            {
                "tournamentUUID":"$tournamentId",
                "golfCourse":"Happy Days Golf Club",
                "competitionName":"South West Invitational",
                "hostCountry":"United States Of America",
                "epochStart":"1638349200",
                "epochFinish":"1638468000",
                "rounds":"2",
                "playerCount":"35"
            }
        """.trimIndent(), SOURCE_2_TOKEN)

        // then
        assertEquals(HttpStatus.OK.value(), response.statusCode())

        val actual = tournamentRepository.findByProviderAndExternalId(Source2PayloadConverter.PROVIDER_NAME, tournamentId)
        assertTrue(actual.isPresent)
        assertEquals(tournamentId, actual.get().externalId)
        assertEquals(Source2PayloadConverter.PROVIDER_NAME, actual.get().provider)
        assertEquals("South West Invitational", actual.get().name)
        assertEquals("Happy Days Golf Club", actual.get().courseName)
        assertEquals("US", actual.get().countryCode)
        assertEquals(LocalDate.of(2021, 12, 1), actual.get().startDate)
        assertEquals(LocalDate.of(2021, 12, 2), actual.get().endDate)
        assertEquals(2, actual.get().rounds)
        assertEquals(35, actual.get().playerCount)
    }

    @Test
    fun `client receives bad request error if payload is in unexpected format`() {
        // given
        val tournamentId = "174638"

        // when
        val response = insertPayload("""
              {
                  "tournamentId": "$tournamentId",
                  "tournamentName": "Women's Open Championship",
                  "forecast": "fair",
                  "courseName": "Sunnydale Golf Course",
                  "countryCode": "GB",
                  "startDate": "09/07/21",
                  "endDate": "13/07/21",
                  "roundCount": "4"
              }
        """.trimIndent(), SOURCE_2_TOKEN)

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode())

        val actual = tournamentRepository.findByExternalId(tournamentId)
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `client receives bad request error if provider token is not provided`() {
        // given
        val tournamentId = "174638"

        // when
        val response = insertPayload("""
              {
                  "tournamentId": "$tournamentId",
                  "tournamentName": "Women's Open Championship",
                  "forecast": "fair",
                  "courseName": "Sunnydale Golf Course",
                  "countryCode": "GB",
                  "startDate": "09/07/21",
                  "endDate": "13/07/21",
                  "roundCount": "4"
              }
        """.trimIndent(), null)

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode())
    }

    @Test
    fun `client receives successful status result if inserts tournament information multiple times`() {
        // given
        val tournamentId = "174638"
        val payload = """
              {
                  "tournamentId": "$tournamentId",
                  "tournamentName": "Women's Open Championship",
                  "forecast": "fair",
                  "courseName": "Sunnydale Golf Course",
                  "countryCode": "GB",
                  "startDate": "09/07/21",
                  "endDate": "13/07/21",
                  "roundCount": "4"
              }
        """.trimIndent()

        val response = insertPayload(payload, SOURCE_1_TOKEN)
        assertEquals(HttpStatus.OK.value(), response.statusCode())

        val original = tournamentRepository.findByProviderAndExternalId(Source1PayloadConverter.PROVIDER_NAME, tournamentId)
        assertTrue(original.isPresent)

        // when
        val secondResponse = insertPayload(payload, SOURCE_1_TOKEN)

        // then
        assertEquals(HttpStatus.OK.value(), secondResponse.statusCode())

        val actual = tournamentRepository.findByProviderAndExternalId(Source1PayloadConverter.PROVIDER_NAME, tournamentId)
        assertTrue(actual.isPresent)

        assertEquals(tournamentId, actual.get().externalId)
        assertEquals(Source1PayloadConverter.PROVIDER_NAME, actual.get().provider)
        assertEquals("Women's Open Championship", actual.get().name)
        assertEquals("fair", actual.get().forecast)
        assertEquals("Sunnydale Golf Course", actual.get().courseName)
        assertEquals("GB", actual.get().countryCode)
        assertEquals(LocalDate.of(2021, 7, 9), actual.get().startDate)
        assertEquals(LocalDate.of(2021, 7, 13), actual.get().endDate)
        assertEquals(4, actual.get().rounds)
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