package com.venikkin.example.golftmts.service

import com.venikkin.example.golftmts.model.Tournament
import com.venikkin.example.golftmts.repository.TournamentRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class TournamentServiceTest {

    @InjectMocks
    private lateinit var tournamentService: TournamentService

    @Mock
    private lateinit var tournamentRepository: TournamentRepository

    @Test
    fun `saveTournament inserts tournament if it doesn't exists`() {
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
        given(tournamentRepository.existsByProviderAndExternalId(tournament.provider, tournament.externalId)).willReturn(false)

        // when
        tournamentService.saveTournament(tournament)

        // then
        verify(tournamentRepository).save(tournament)
    }

    @Test
    fun `saveTournament doesn't insert tournament if it exists`() {
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
        given(tournamentRepository.existsByProviderAndExternalId(tournament.provider, tournament.externalId)).willReturn(true)

        // when
        tournamentService.saveTournament(tournament)

        // then
        verify(tournamentRepository, never()).save(tournament)
    }

}