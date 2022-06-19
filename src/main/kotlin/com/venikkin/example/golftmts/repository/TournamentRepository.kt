package com.venikkin.example.golftmts.repository

import com.venikkin.example.golftmts.model.Tournament
import org.springframework.data.repository.CrudRepository

interface TournamentRepository: CrudRepository<Tournament, Long> {

    fun existsByProviderAndExternalId(provider: String, externalId: String): Boolean

}