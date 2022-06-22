package com.venikkin.example.golftmts.provider.source1

import com.venikkin.example.golftmts.configuration.InvalidPayloadException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class Source1PayloadConverterTest {

    private val source1PayloadConverter = Source1PayloadConverter()

    @Test
    fun `can deserialise payload`() {
        // when
        val result = source1PayloadConverter.convert("""
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
        """.trimIndent())

        // then
        assertEquals("174638", result.externalId)
        assertEquals("Women's Open Championship", result.name)
        assertEquals("fair", result.forecast)
        assertEquals("Sunnydale Golf Course", result.courseName)
        assertEquals("GB", result.countryCode)
        assertEquals(LocalDate.of(2021, 7, 9), result.startDate)
        assertEquals(LocalDate.of(2021, 7, 13), result.endDate)
        assertEquals(4, result.rounds)
    }

    @Test
    fun `returns invalid payload exception when mandatory field is missing`() {
        assertThrows(InvalidPayloadException::class.java) {
            source1PayloadConverter.convert("""
                {
                    "tournamentName": "Women's Open Championship",
                    "forecast": "fair",
                    "courseName": "Sunnydale Golf Course",
                    "countryCode": "GB",
                    "startDate": "09/07/21",
                    "endDate": "13/07/21",
                    "roundCount": "4"
                }
                """.trimIndent())
        }
    }

    @Test
    fun `returns invalid payload exception when payload is not a JSON`() {
        assertThrows(InvalidPayloadException::class.java) {
            source1PayloadConverter.convert("tournamentId=174638&" +
            "tournamentName=Women%27s%20Open%20Championship&" +
            "forecast=fair&" +
            "courseName=Sunnydale%20Golf%20Course&" +
            "countryCode=GB&" +
            "startDate=09%2F07%2F21&" +
            "endDate=09%2F07%2F21&roundCount=4")
        }
    }

    @Test
    fun `returns invalid payload exception field value is not in expected format`() {
        assertThrows(InvalidPayloadException::class.java) {
            source1PayloadConverter.convert("""
                {
                    "tournamentName": "Women's Open Championship",
                    "forecast": "fair",
                    "courseName": "Sunnydale Golf Course",
                    "countryCode": "GB",
                    "startDate": "1655891483",
                    "endDate": "1655891483",
                    "roundCount": "4"
                }
                """.trimIndent())
        }
    }

    @Test
    fun `convert ignores additional fields`() {
        // when
        val result = source1PayloadConverter.convert("""
        {
            "tournamentId": "174638",
            "tournamentName": "Women's Open Championship",
            "forecast": "fair",
            "courseName": "Sunnydale Golf Course",
            "countryCode": "GB",
            "startDate": "09/07/21",
            "endDate": "13/07/21",
            "roundCount": "4",
            "weather": "perfect"
        }
        """.trimIndent())

        // then
        assertEquals("174638", result.externalId)
        assertEquals("Women's Open Championship", result.name)
        assertEquals("fair", result.forecast)
        assertEquals("Sunnydale Golf Course", result.courseName)
        assertEquals("GB", result.countryCode)
        assertEquals(LocalDate.of(2021, 7, 9), result.startDate)
        assertEquals(LocalDate.of(2021, 7, 13), result.endDate)
        assertEquals(4, result.rounds)
    }

}