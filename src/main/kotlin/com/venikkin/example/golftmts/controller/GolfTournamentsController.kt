package com.venikkin.example.golftmts.controller

import com.venikkin.example.golftmts.provider.Provider
import com.venikkin.example.golftmts.provider.ProviderPayloadConverter
import com.venikkin.example.golftmts.service.TournamentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        private val tournamentService: TournamentService
) {

    companion object {
        val log:Logger = LoggerFactory.getLogger(GolfTournamentsController::class.java)
    }

    @PostMapping(consumes = [ "application/json" ])
    @ResponseStatus(OK)
    fun postGolfTournament(@RequestBody payload: String,
                           @Provider payloadConverter: ProviderPayloadConverter) {
        val tournament = payloadConverter.convert(payload)
        log.info("Received {}", tournament)
        tournamentService.saveTournament(tournament)
    }

}