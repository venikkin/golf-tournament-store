package com.venikkin.example.golftmts.provider.source1

import com.venikkin.example.golftmts.configuration.BadRequestException
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
    fun `returns bad request exception when fail to parse payload`() {
        assertThrows(BadRequestException::class.java) {
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

}