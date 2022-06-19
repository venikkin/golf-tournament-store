package com.venikkin.example.golftmts.service

import com.venikkin.example.golftmts.model.Tournament
import com.venikkin.example.golftmts.repository.TournamentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TournamentService @Autowired constructor(
        private val tournamentRepository: TournamentRepository
) {

    @Transactional
    open fun saveTournament(tournament: Tournament) {
        if (tournamentRepository.existsByProviderAndExternalId(tournament.provider, tournament.externalId)) {
            return;
        }
        tournamentRepository.save(tournament)
    }

}