package com.venikkin.example.golftmts.repository

import com.venikkin.example.golftmts.model.Tournament
import org.springframework.data.repository.CrudRepository
import java.util.*

interface TournamentRepository: CrudRepository<Tournament, Long> {

    fun existsByProviderAndExternalId(provider: String, externalId: String): Boolean

    fun findByProviderAndExternalId(provider: String, externalId: String): Optional<Tournament>

    fun findByExternalId(externalId: String): List<Tournament>

}