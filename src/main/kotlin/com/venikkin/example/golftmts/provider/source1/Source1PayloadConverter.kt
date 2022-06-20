package com.venikkin.example.golftmts.provider.source1

import com.google.gson.Gson
import com.venikkin.example.golftmts.configuration.BadRequestException
import com.venikkin.example.golftmts.model.Tournament
import com.venikkin.example.golftmts.provider.ProviderPayloadConverter
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class Source1PayloadConverter: ProviderPayloadConverter {

    companion object {
        const val PROVIDER_NAME = "Source1"
        private val GSON: Gson = Gson()
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    }

    override fun convert(payload: String): Tournament {
        val source1Payload = GSON.fromJson(payload, Source1Payload::class.java) ?: throw BadRequestException("Fail to deserialise payload")
        return try {
            Tournament(
                name = source1Payload.tournamentName,
                courseName = source1Payload.courseName,
                // this field potentially need to normalised as internal country code may not match external representation
                countryCode = source1Payload.countryCode,
                forecast = source1Payload.forecast,
                startDate = LocalDate.from(DATE_FORMAT.parse(source1Payload.startDate)),
                endDate = LocalDate.from(DATE_FORMAT.parse(source1Payload.endDate)),
                rounds = Integer.parseInt(source1Payload.roundCount),
                externalId = source1Payload.tournamentId,
                provider = PROVIDER_NAME
            )
        } catch (ex: Exception) {
            throw BadRequestException("Fail to deserialise payload", ex)
        }
    }

    data class Source1Payload(
            val tournamentId: String,
            val tournamentName: String,
            val forecast: String,
            val courseName: String,
            val countryCode: String,
            val startDate: String,
            val endDate: String,
            val roundCount: String // could be a number
    )

}