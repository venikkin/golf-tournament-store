package com.venikkin.example.golftmts.provider.source2

import com.google.gson.Gson
import com.venikkin.example.golftmts.configuration.BadRequestException
import com.venikkin.example.golftmts.model.Tournament
import com.venikkin.example.golftmts.provider.ProviderPayloadConverter
import com.venikkin.example.golftmts.service.CountryCodeConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class Source2PayloadConverter @Autowired constructor(
    private val countryCodeConverter: CountryCodeConverter
): ProviderPayloadConverter {

    companion object {
        const val PROVIDER_NAME = "Source2"
        private val GSON: Gson = Gson()

        private fun toUtcLocalDate(s: String) = Date(s.toLong() * 1000)
            .toInstant()
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
    }

    override fun convert(payload: String): Tournament {
        val source2Payload = GSON.fromJson(payload, Source2Payload::class.java) ?: throw BadRequestException("Fail to deserialise payload")
        return try {
            Tournament(
                name = source2Payload.competitionName,
                courseName = source2Payload.golfCourse,
                startDate = toUtcLocalDate(source2Payload.epochStart),
                endDate = toUtcLocalDate(source2Payload.epochFinish),
                rounds = source2Payload.rounds,
                playerCount = source2Payload.playerCount,
                externalId = source2Payload.tournamentUUID,
                provider = PROVIDER_NAME,
                countryCode = countryCodeConverter.convertToCountryCode(source2Payload.hostCountry)
            )
        } catch (ex: Exception) {
            throw BadRequestException("Fail to deserialise payload", ex)
        }
    }


    data class Source2Payload(
        val tournamentUUID: String,
        val golfCourse: String,
        val competitionName: String,
        val hostCountry: String,
        val epochStart: String, // could be a number
        val epochFinish: String, // could be a number
        val rounds: Int,
        val playerCount: Int
    )
}