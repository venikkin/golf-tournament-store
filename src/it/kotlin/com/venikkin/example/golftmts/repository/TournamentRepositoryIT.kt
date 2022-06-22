package com.venikkin.example.golftmts.repository

import com.venikkin.example.golftmts.BaseIT
import com.venikkin.example.golftmts.model.Tournament
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

class TournamentRepositoryIT: BaseIT() {

    @Autowired
    private lateinit var tournamentRepository: TournamentRepository

    @AfterEach
    fun cleanUp() {
        tournamentRepository.deleteAll()
    }

    @Test
    fun `can save tournament`() {
        // given
        val tournament = Tournament(
            name = "Tournament name",
            courseName = "Course name",
            countryCode = "LT",
            startDate = LocalDate.of(2022, 1, 7),
            endDate = LocalDate.of(2022, 1, 15),
            rounds = 13,
            playerCount = 42,
            forecast = "fair",
            externalId = UUID.randomUUID().toString(),
            provider = "TestProvider"
        )

        // when
        val saved = tournamentRepository.save(tournament)

        // then
        assertNotNull(saved.id)
        val actual = tournamentRepository.findById(saved.id!!)
        assertTrue(actual.isPresent)
        assertEquals(tournament.countryCode, actual.get().countryCode)
        assertEquals(tournament.name, actual.get().name)
        assertEquals(tournament.courseName, actual.get().courseName)
        assertEquals(tournament.startDate, actual.get().startDate)
        assertEquals(tournament.endDate, actual.get().endDate)
        assertEquals(tournament.rounds, actual.get().rounds)
        assertEquals(tournament.playerCount, actual.get().playerCount)
        assertEquals(tournament.forecast, actual.get().forecast)
        assertEquals(tournament.externalId, actual.get().externalId)
        assertEquals(tournament.provider, actual.get().provider)
    }

    @Test
    fun `exist by provider and external id returns true when record exists`() {
        // given
        val tournament1 = Tournament(
            name = "Tournament name 1",
            courseName = "Course name 2",
            countryCode = "LT",
            startDate = LocalDate.of(2022, 1, 7),
            endDate = LocalDate.of(2022, 1, 15),
            rounds = 13,
            playerCount = 42,
            forecast = "fair",
            externalId = UUID.randomUUID().toString(),
            provider = "TestProvider"
        )

        val tournament2 = Tournament(
            name = "Tournament name 1",
            courseName = "Course name 2",
            countryCode = "LT",
            startDate = LocalDate.of(2022, 1, 7),
            endDate = LocalDate.of(2022, 1, 15),
            rounds = 13,
            playerCount = 42,
            forecast = "fair",
            externalId = UUID.randomUUID().toString(),
            provider = "TestProvider"
        )

        tournamentRepository.saveAll(listOf(tournament1, tournament2))

        // when
        val actual = tournamentRepository.existsByProviderAndExternalId(tournament1.provider, tournament1.externalId)

        // then
        assertTrue(actual)
    }

    @Test
    fun `exist by provider and external id returns false when record doesn't exists`() {
        // given
        val tournament = Tournament(
            name = "Tournament name 1",
            courseName = "Course name 2",
            countryCode = "LT",
            startDate = LocalDate.of(2022, 1, 7),
            endDate = LocalDate.of(2022, 1, 15),
            rounds = 13,
            playerCount = 42,
            forecast = "fair",
            externalId = UUID.randomUUID().toString(),
            provider = "TestProvider"
        )

        tournamentRepository.save(tournament)

        // when
        val actual = tournamentRepository.existsByProviderAndExternalId(tournament.provider, "non-existing-id")

        // then
        assertFalse(actual)
    }

}