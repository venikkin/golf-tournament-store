package com.venikkin.example.golftmts.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController("/golf/tournaments")
class GolfTournamentsController {

    @PostMapping
    fun postGolfTournament(@RequestBody payload: Any?) {
    }

}