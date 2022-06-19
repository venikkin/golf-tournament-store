package com.venikkin.example.golftmts.controller

import com.venikkin.example.golftmts.provider.Provider
import com.venikkin.example.golftmts.provider.ProviderPayloadConverter
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RestController
@RequestMapping("/golf/tournaments")
class GolfTournamentsController {

    @PostMapping
    @ResponseStatus(OK)
    fun postGolfTournament(@RequestBody payload: String,
                           @Provider payloadConverter: ProviderPayloadConverter) {

    }

    @GetMapping
    @ResponseStatus(OK)
    fun ping(): String {
        return "{\"hello\": \"world\"}"
    }

}