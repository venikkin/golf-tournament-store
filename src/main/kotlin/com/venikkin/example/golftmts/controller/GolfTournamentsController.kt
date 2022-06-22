package com.venikkin.example.golftmts.controller

import com.venikkin.example.golftmts.provider.Provider
import com.venikkin.example.golftmts.provider.ProviderPayloadConverter
import com.venikkin.example.golftmts.provider.Providers.ProviderSetting
import com.venikkin.example.golftmts.service.TournamentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RestController
@RequestMapping("/golf/tournaments")
class GolfTournamentsController @Autowired constructor(
        private val converters: Map<String, ProviderPayloadConverter>,
        private val tournamentService: TournamentService
) {

    @PostMapping(consumes = [ "application/json" ])
    @ResponseStatus(OK)
    fun postGolfTournament(@RequestBody payload: String, @Provider provider: ProviderSetting) {
        val payloadConverter = converters[provider.payloadConverter] ?: throw IllegalStateException("Fail to find payload converter")
        val tournament = payloadConverter.convert(payload)
        tournamentService.saveTournament(tournament)
    }

}