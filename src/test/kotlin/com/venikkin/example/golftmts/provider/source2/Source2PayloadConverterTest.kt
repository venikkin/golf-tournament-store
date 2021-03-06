package com.venikkin.example.golftmts.provider.source2

import com.venikkin.example.golftmts.configuration.InvalidPayloadException
import com.venikkin.example.golftmts.service.CountryCodeConverter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class Source2PayloadConverterTest {

    @InjectMocks
    lateinit var source2PayloadConverter: Source2PayloadConverter

    @Mock
    lateinit var countryCodeConverter: CountryCodeConverter

    @Test
    fun `can deserialise payload`() {
        // given
        given(countryCodeConverter.convertToCountryCode("United States Of America")).willReturn("US")

        // when
        val result = source2PayloadConverter.convert("""
            {
                "tournamentUUID":"87fc6650-e114-4179-9aef-6a9a13030f80",
                "golfCourse":"Happy Days Golf Club",
                "competitionName":"South West Invitational",
                "hostCountry":"United States Of America",
                "epochStart":"1638349200",
                "epochFinish":"1638468000",
                "rounds":"2",
                "playerCount":"35"
            }
        """.trimIndent())

        // then
        assertEquals("87fc6650-e114-4179-9aef-6a9a13030f80", result.externalId)
        assertEquals("South West Invitational", result.name)
        assertEquals("Happy Days Golf Club", result.courseName)
        assertEquals("US", result.countryCode)
        assertEquals(LocalDate.of(2021, 12, 1), result.startDate)
        assertEquals(LocalDate.of(2021, 12, 2), result.endDate)
        assertEquals(2, result.rounds)
        assertEquals(35, result.playerCount)
    }

    @Test
    fun `returns invalid payload exception when mandatory field is missing`() {
        Assertions.assertThrows(InvalidPayloadException::class.java) {
            source2PayloadConverter.convert("""
            {
                "golfCourse":"Happy Days Golf Club",
                "competitionName":"South West Invitational",
                "hostCountry":"United States Of America",
                "epochStart":"1638349200",
                "epochFinish":"1638468000",
                "rounds":"2",
                "playerCount":"35"
            }
            """.trimIndent())
        }
    }

    @Test
    fun `returns invalid payload exception when payload is not a JSON`() {
        Assertions.assertThrows(InvalidPayloadException::class.java) {
            source2PayloadConverter.convert("tournamentUUID=87fc6650-e114-4179-9aef-6a9a13030f80&" +
                "golfCourse=Happy%20Days%20Golf%20Club&" +
                "competitionName=South%20West%20Invitational&" +
                "hostCountry=United%20States%20Of%20America&" +
                "epochStart=1638349200&" +
                "epochFinish=1638468000" +
                "rounds=2&playerCount=35")
        }
    }

    @Test
    fun `returns invalid payload exception field value is not in expected format`() {
        Assertions.assertThrows(InvalidPayloadException::class.java) {
            source2PayloadConverter.convert("""
            {
                "tournamentUUID":"87fc6650-e114-4179-9aef-6a9a13030f80",
                "golfCourse":"Happy Days Golf Club",
                "competitionName":"South West Invitational",
                "hostCountry":"United States Of America",
                "epochStart":"9/7/21",
                "epochFinish":"13/7/21",
                "rounds":"2",
                "playerCount":"35"
            }
            """.trimIndent())
        }
    }

    @Test
    fun `convert ignores additional fields`() {
        // given
        given(countryCodeConverter.convertToCountryCode("United States Of America")).willReturn("US")

        // when
        val result = source2PayloadConverter.convert("""
            {
                "tournamentUUID":"87fc6650-e114-4179-9aef-6a9a13030f80",
                "golfCourse":"Happy Days Golf Club",
                "competitionName":"South West Invitational",
                "hostCountry":"United States Of America",
                "epochStart":"1638349200",
                "epochFinish":"1638468000",
                "rounds":"2",
                "playerCount":"35",
                "latitude": 123456
            }
        """.trimIndent())

        // then
        assertEquals("87fc6650-e114-4179-9aef-6a9a13030f80", result.externalId)
        assertEquals("South West Invitational", result.name)
        assertEquals("Happy Days Golf Club", result.courseName)
        assertEquals("US", result.countryCode)
        assertEquals(LocalDate.of(2021, 12, 1), result.startDate)
        assertEquals(LocalDate.of(2021, 12, 2), result.endDate)
        assertEquals(2, result.rounds)
        assertEquals(35, result.playerCount)
    }

}